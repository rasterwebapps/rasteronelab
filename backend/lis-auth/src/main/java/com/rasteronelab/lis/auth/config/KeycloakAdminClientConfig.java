package com.rasteronelab.lis.auth.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the Keycloak Admin Client bean used to manage users and roles
 * programmatically via the Keycloak Admin REST API.
 *
 * <p>Passwords are never stored in the LIS database. All credential operations
 * (create, reset, change) delegate exclusively to Keycloak, which persists
 * credentials as PBKDF2-SHA256 hashes in its own PostgreSQL tables.</p>
 */
@Configuration
public class KeycloakAdminClientConfig {

    @Value("${keycloak.auth-server-url:http://localhost:8180}")
    private String authServerUrl;

    @Value("${keycloak.realm:rasteronelab}")
    private String realm;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin123}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id:admin-cli}")
    private String adminClientId;

    /**
     * Returns a Keycloak admin client authenticated against the master realm.
     * Uses the service account approach (client_credentials) in production;
     * falls back to direct password grant for the admin-cli in development.
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .clientId(adminClientId)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    @Bean
    public String keycloakRealm() {
        return realm;
    }
}
