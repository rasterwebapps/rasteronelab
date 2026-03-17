package com.rasteronelab.lis.auth.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Converts a Keycloak-issued JWT into a {@link LisAuthenticationToken} that carries
 * both standard roles (realm + client) and LIS-specific custom claims:
 * {@code organization_id}, {@code branch_ids}, and {@code employee_id}.
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLES_KEY = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ORGANIZATION_ID_CLAIM = "organizationId";
    private static final String BRANCH_IDS_CLAIM = "branchIds";
    private static final String EMPLOYEE_ID_CLAIM = "employeeId";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                extractRealmRoles(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).toList();

        String username = jwt.getClaimAsString("preferred_username");
        String organizationId = jwt.getClaimAsString(ORGANIZATION_ID_CLAIM);
        List<String> branchIds = extractBranchIds(jwt);
        String employeeId = jwt.getClaimAsString(EMPLOYEE_ID_CLAIM);

        return new LisAuthenticationToken(jwt, authorities, username,
                organizationId, branchIds, employeeId);
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        if (realmAccess == null || !realmAccess.containsKey(ROLES_KEY)) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get(ROLES_KEY);
        return roles.stream()
                .<GrantedAuthority>map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM);
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        return resourceAccess.values().stream()
                .filter(Map.class::isInstance)
                .map(client -> (Map<String, Object>) client)
                .filter(client -> client.containsKey(ROLES_KEY))
                .flatMap(client -> ((List<String>) client.get(ROLES_KEY)).stream())
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<String> extractBranchIds(Jwt jwt) {
        Object branchIdsObj = jwt.getClaim(BRANCH_IDS_CLAIM);
        if (branchIdsObj instanceof List<?> list) {
            return list.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }
        return Collections.emptyList();
    }
}
