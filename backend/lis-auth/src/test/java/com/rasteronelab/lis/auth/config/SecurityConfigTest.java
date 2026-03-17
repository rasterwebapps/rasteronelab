package com.rasteronelab.lis.auth.config;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link SecurityConfig}.
 * Validates JWT-based security rules and the Keycloak JWT authentication converter.
 */
@SpringBootTest(classes = SecurityConfigTest.TestApp.class)
@AutoConfigureMockMvc
class SecurityConfigTest {

    /**
     * Minimal test application that scans only the auth package,
     * avoiding the JPA auditing config in lis-core.
     */
    @Configuration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            JpaRepositoriesAutoConfiguration.class,
            RedisAutoConfiguration.class
    })
    @ComponentScan(basePackages = "com.rasteronelab.lis.auth.config")
    static class TestApp {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    // ── SecurityFilterChain tests ────────────────────────────────────────

    @Nested
    @DisplayName("SecurityFilterChain authorization rules")
    class AuthorizationRulesTests {

        @Test
        @DisplayName("Unauthenticated request to protected endpoint returns 401")
        void unauthenticatedRequestShouldReturn401() throws Exception {
            mockMvc.perform(get("/api/v1/protected-resource"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Actuator health endpoint is accessible without authentication")
        void actuatorHealthShouldBeAccessibleWithoutAuth() throws Exception {
            mockMvc.perform(get("/actuator/health"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("API docs endpoint is accessible without authentication")
        void apiDocsShouldBeAccessibleWithoutAuth() throws Exception {
            // No 401 — the endpoint is permitted even without a JWT.
            // Returns 404 in test context because springdoc is not loaded.
            mockMvc.perform(get("/api-docs"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Valid JWT with roles is accepted (returns 404, not 401)")
        void authenticatedRequestWithRolesShouldNotReturn401() throws Exception {
            mockMvc.perform(get("/api/v1/protected-resource")
                            .with(jwt().authorities(
                                    new SimpleGrantedAuthority("ROLE_LAB_TECHNICIAN"))))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Valid JWT without roles is still accepted for authenticated endpoints")
        void authenticatedRequestWithoutRolesShouldNotReturn401() throws Exception {
            mockMvc.perform(get("/api/v1/protected-resource")
                            .with(jwt()))
                    .andExpect(status().isNotFound());
        }
    }

    // ── KeycloakJwtAuthenticationConverter unit tests ────────────────────

    @Nested
    @DisplayName("KeycloakJwtAuthenticationConverter")
    class ConverterTests {

        private final KeycloakJwtAuthenticationConverter converter =
                new KeycloakJwtAuthenticationConverter();

        @Test
        @DisplayName("Extracts realm roles from JWT")
        void shouldExtractRealmRoles() {
            Jwt jwt = buildJwt(Map.of(
                    "realm_access", Map.of("roles", List.of("ADMIN", "LAB_TECHNICIAN"))
            ));

            var token = converter.convert(jwt);

            assertThat(token).isNotNull();
            assertThat(token.getAuthorities())
                    .extracting(a -> a.getAuthority())
                    .contains("ROLE_ADMIN", "ROLE_LAB_TECHNICIAN");
        }

        @Test
        @DisplayName("Extracts resource (client) roles from JWT")
        void shouldExtractResourceRoles() {
            Jwt jwt = buildJwt(Map.of(
                    "resource_access", Map.of(
                            "lis-auth", Map.of("roles", List.of("manage-users")),
                            "lis-patient", Map.of("roles", List.of("view-patients"))
                    )
            ));

            var token = converter.convert(jwt);

            assertThat(token).isNotNull();
            assertThat(token.getAuthorities())
                    .extracting(a -> a.getAuthority())
                    .contains("ROLE_manage-users", "ROLE_view-patients");
        }

        @Test
        @DisplayName("Extracts combined realm and resource roles")
        void shouldExtractCombinedRoles() {
            Jwt jwt = buildJwt(Map.of(
                    "realm_access", Map.of("roles", List.of("ADMIN")),
                    "resource_access", Map.of(
                            "lis-auth", Map.of("roles", List.of("manage-users"))
                    )
            ));

            var token = converter.convert(jwt);

            assertThat(token).isNotNull();
            assertThat(token.getAuthorities())
                    .extracting(a -> a.getAuthority())
                    .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_manage-users");
        }

        @Test
        @DisplayName("Extracts custom LIS claims (organizationId, branchIds, employeeId)")
        void shouldExtractCustomLisClaims() {
            Jwt jwt = buildJwt(Map.of(
                    "organizationId", "org-001",
                    "branchIds", List.of("branch-aaa", "branch-bbb"),
                    "employeeId", "emp-999",
                    "preferred_username", "john.doe"
            ));

            var token = converter.convert(jwt);

            assertThat(token).isInstanceOf(LisAuthenticationToken.class);

            var lisToken = (LisAuthenticationToken) token;
            assertThat(lisToken.getOrganizationId()).isEqualTo("org-001");
            assertThat(lisToken.getBranchIds()).containsExactly("branch-aaa", "branch-bbb");
            assertThat(lisToken.getEmployeeId()).isEqualTo("emp-999");
            assertThat(lisToken.getName()).isEqualTo("john.doe");
        }

        @Test
        @DisplayName("Handles JWT with no custom claims gracefully")
        void shouldHandleMissingCustomClaims() {
            Jwt jwt = buildJwt(Map.of());

            var token = converter.convert(jwt);

            assertThat(token).isInstanceOf(LisAuthenticationToken.class);

            var lisToken = (LisAuthenticationToken) token;
            assertThat(lisToken.getOrganizationId()).isNull();
            assertThat(lisToken.getBranchIds()).isEmpty();
            assertThat(lisToken.getEmployeeId()).isNull();
            assertThat(lisToken.getAuthorities()).isEmpty();
        }

        @Test
        @DisplayName("Handles JWT with no roles gracefully")
        void shouldHandleMissingRoles() {
            Jwt jwt = buildJwt(Map.of(
                    "realm_access", Map.of("other", "value"),
                    "resource_access", Map.of("client", Map.of("other", "value"))
            ));

            var token = converter.convert(jwt);

            assertThat(token).isNotNull();
            assertThat(token.getAuthorities()).isEmpty();
        }

        private Jwt buildJwt(Map<String, Object> additionalClaims) {
            var builder = Jwt.withTokenValue("mock-token")
                    .header("alg", "RS256")
                    .claim("sub", "user-123")
                    .issuer("https://keycloak.test/realms/rasteronelab")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600));

            additionalClaims.forEach(builder::claim);
            return builder.build();
        }
    }
}
