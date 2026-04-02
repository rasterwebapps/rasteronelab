package com.rasteronelab.lis.auth.api.rest;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import com.rasteronelab.lis.auth.application.service.TokenBlacklistService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link TokenController}.
 */
@SpringBootTest(classes = TokenControllerTest.TestApp.class)
@AutoConfigureMockMvc
class TokenControllerTest {

    @Configuration
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            JpaRepositoriesAutoConfiguration.class,
            RedisAutoConfiguration.class
    })
    @ComponentScan(basePackages = {
            "com.rasteronelab.lis.auth.config",
            "com.rasteronelab.lis.auth.api.rest"
    })
    static class TestApp {
    }

    private static final String TEST_RAW_TOKEN = "mock.token.value";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @MockBean
    @SuppressWarnings("unused")
    private com.rasteronelab.lis.auth.application.service.KeycloakAdminService keycloakAdminService;

    @BeforeEach
    void setUp() {
        Jwt jwt = Jwt.withTokenValue(TEST_RAW_TOKEN)
                .header("alg", "RS256")
                .claim("sub", "user-123")
                .claim("jti", "jti-001")
                .issuer("https://keycloak.test/realms/rasteronelab")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout returns 200 for authenticated user")
    void logoutShouldReturn200ForAuthenticatedUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + TEST_RAW_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt()
                                .jwt(b -> b.claim("jti", "jti-001")
                                        .claim("sub", "user-123")
                                        .issuedAt(Instant.now())
                                        .expiresAt(Instant.now().plusSeconds(3600)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logged out successfully"));

        verify(tokenBlacklistService).blacklist(any(Jwt.class), eq(TEST_RAW_TOKEN));
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout returns 401 without authentication")
    void logoutShouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}

