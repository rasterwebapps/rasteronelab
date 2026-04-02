package com.rasteronelab.lis.auth.application.service;

import java.util.Collections;
import java.util.List;

import jakarta.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rasteronelab.lis.auth.api.dto.CreateUserRequest;
import com.rasteronelab.lis.auth.api.dto.UserResponse;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;

/**
 * Manages Keycloak users on behalf of the LIS application.
 *
 * <p><strong>Password handling policy:</strong> Passwords are <em>never</em> stored inside
 * the LIS database. Every credential operation (create, reset, temporary password) is
 * delegated directly to the Keycloak Admin REST API. Keycloak hashes all passwords with
 * PBKDF2-SHA256 before persisting them in its own PostgreSQL tables.</p>
 */
@Service
public class KeycloakAdminService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminService.class);

    private final Keycloak keycloak;
    private final String realm;

    public KeycloakAdminService(Keycloak keycloak,
                                @Qualifier("keycloakRealm") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    /**
     * Creates a new user in Keycloak.
     *
     * <p>The initial password is set as a temporary credential so the user must change
     * it on first login. The raw password is forwarded directly to Keycloak over TLS
     * and is never written to any LIS table.</p>
     *
     * @param request the create-user payload (username, email, firstName, lastName, password, roles)
     * @return the created user's profile
     * @throws DuplicateResourceException if the username or email already exists
     */
    public UserResponse createUser(CreateUserRequest request) {
        UsersResource usersResource = realmResource().users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == 409) {
                throw new DuplicateResourceException("User", "username", request.getUsername());
            }
            if (response.getStatus() != 201) {
                log.error("Keycloak user creation failed: HTTP {}", response.getStatus());
                throw new IllegalStateException("Keycloak user creation failed with status: "
                        + response.getStatus());
            }

            String userId = extractCreatedId(response);

            setTemporaryPassword(userId, request.getTemporaryPassword());

            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                assignRealmRoles(userId, request.getRoles());
            }

            log.info("Created Keycloak user: username={}, userId={}", request.getUsername(), userId);
            return getUserById(userId);
        }
    }

    /**
     * Retrieves a user from Keycloak by their internal user ID.
     *
     * @param userId Keycloak user UUID
     * @return the user's profile DTO
     * @throws NotFoundException if no user exists with that ID
     */
    public UserResponse getUserById(String userId) {
        try {
            UserRepresentation user = realmResource().users().get(userId).toRepresentation();
            return toResponse(user);
        } catch (jakarta.ws.rs.NotFoundException e) {
            throw new NotFoundException("User", userId);
        }
    }

    /**
     * Lists all users in the realm (paginated).
     *
     * @param first  zero-based offset
     * @param max    maximum number of results
     * @return list of user profiles
     */
    public List<UserResponse> listUsers(int first, int max) {
        return realmResource().users()
                .list(first, max)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Resets a user's password via the Keycloak Admin API.
     *
     * <p>The new password is transmitted over TLS directly to Keycloak.
     * Keycloak stores it hashed (PBKDF2-SHA256 + random salt) — the LIS service
     * never persists the password value anywhere.</p>
     *
     * @param userId      Keycloak user UUID
     * @param newPassword the new plain-text password (transmitted over TLS, never stored)
     * @param temporary   if {@code true}, the user must change it on next login
     * @throws NotFoundException if no user exists with that ID
     */
    public void resetPassword(String userId, String newPassword, boolean temporary) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(newPassword);
            credential.setTemporary(temporary);

            realmResource().users().get(userId).resetPassword(credential);
            log.info("Password reset for userId={}, temporary={}", userId, temporary);
        } catch (jakarta.ws.rs.NotFoundException e) {
            throw new NotFoundException("User", userId);
        }
    }

    /**
     * Enables or disables a user account in Keycloak.
     *
     * @param userId  Keycloak user UUID
     * @param enabled {@code true} to enable, {@code false} to disable
     * @throws NotFoundException if no user exists with that ID
     */
    public void setUserEnabled(String userId, boolean enabled) {
        try {
            UserRepresentation user = realmResource().users().get(userId).toRepresentation();
            user.setEnabled(enabled);
            realmResource().users().get(userId).update(user);
            log.info("User {} {}: userId={}", enabled ? "enabled" : "disabled",
                    user.getUsername(), userId);
        } catch (jakarta.ws.rs.NotFoundException e) {
            throw new NotFoundException("User", userId);
        }
    }

    /**
     * Deletes a user from Keycloak.
     *
     * @param userId Keycloak user UUID
     * @throws NotFoundException if no user exists with that ID
     */
    public void deleteUser(String userId) {
        try (Response response = realmResource().users().delete(userId)) {
            if (response.getStatus() == 404) {
                throw new NotFoundException("User", userId);
            }
            log.info("Deleted Keycloak user: userId={}", userId);
        }
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void setTemporaryPassword(String userId, String password) {
        if (password == null || password.isBlank()) {
            return;
        }
        resetPassword(userId, password, true);
    }

    private void assignRealmRoles(String userId, List<String> roleNames) {
        RealmResource realm = realmResource();
        List<RoleRepresentation> roles = roleNames.stream()
                .map(name -> {
                    try {
                        return realm.roles().get(name).toRepresentation();
                    } catch (jakarta.ws.rs.NotFoundException e) {
                        log.warn("Realm role not found, skipping: {}", name);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        if (!roles.isEmpty()) {
            realm.users().get(userId).roles().realmLevel().add(roles);
        }
    }

    private String extractCreatedId(Response response) {
        String location = response.getLocation().toString();
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private RealmResource realmResource() {
        return keycloak.realm(realm);
    }

    private UserResponse toResponse(UserRepresentation user) {
        List<String> roles = Collections.emptyList();
        try {
            roles = realmResource().users().get(user.getId())
                    .roles().realmLevel().listEffective()
                    .stream()
                    .map(RoleRepresentation::getName)
                    .toList();
        } catch (Exception e) {
            log.debug("Could not load roles for user {}: {}", user.getId(), e.getMessage());
        }

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                Boolean.TRUE.equals(user.isEnabled()),
                Boolean.TRUE.equals(user.isEmailVerified()),
                roles
        );
    }
}
