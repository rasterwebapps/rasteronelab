package com.rasteronelab.lis.auth.application.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TokenBlacklistService}.
 */
@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    private TokenBlacklistService service;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
        service = new TokenBlacklistService(redisTemplate);
    }

    @Nested
    @DisplayName("blacklist()")
    class BlacklistTests {

        @Test
        @DisplayName("Stores token in Redis with correct key and TTL when jti is present")
        void shouldStoreTokenWithJtiKey() {
            Jwt jwt = buildJwt("jti-001", Instant.now().plusSeconds(3600));
            String rawToken = buildRawToken("jti-001");

            service.blacklist(jwt, rawToken);

            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            verify(valueOps).set(keyCaptor.capture(), eq("revoked"), any());
            assertThat(keyCaptor.getValue()).isEqualTo("auth:blacklist:jti-001");
        }

        @Test
        @DisplayName("Skips storing already-expired token")
        void shouldSkipExpiredToken() {
            Jwt jwt = buildJwt("jti-expired", Instant.now().minusSeconds(120), Instant.now().minusSeconds(60));
            String rawToken = buildRawToken("jti-expired");

            service.blacklist(jwt, rawToken);

            verify(valueOps, never()).set(any(), any(), any());
        }

        @Test
        @DisplayName("Falls back to token hashCode as key when jti is absent")
        void shouldFallbackToHashCodeKeyWhenJtiAbsent() {
            Jwt jwt = buildJwtNoJti(Instant.now().plusSeconds(3600));
            String rawToken = "raw.token.without.jti";

            service.blacklist(jwt, rawToken);

            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            verify(valueOps).set(keyCaptor.capture(), eq("revoked"), any());
            assertThat(keyCaptor.getValue())
                    .startsWith("auth:blacklist:");
        }
    }

    @Nested
    @DisplayName("isBlacklisted()")
    class IsBlacklistedTests {

        @Test
        @DisplayName("Returns true when token key exists in Redis")
        void shouldReturnTrueWhenKeyExists() {
            String rawToken = buildRawToken("jti-abc");
            when(redisTemplate.hasKey("auth:blacklist:jti-abc")).thenReturn(true);

            assertThat(service.isBlacklisted(rawToken)).isTrue();
        }

        @Test
        @DisplayName("Returns false when token key is absent from Redis")
        void shouldReturnFalseWhenKeyAbsent() {
            String rawToken = buildRawToken("jti-xyz");
            when(redisTemplate.hasKey("auth:blacklist:jti-xyz")).thenReturn(false);

            assertThat(service.isBlacklisted(rawToken)).isFalse();
        }

        @Test
        @DisplayName("Returns false (safe default) when Redis call throws")
        void shouldReturnFalseOnRedisError() {
            when(redisTemplate.hasKey(any())).thenThrow(new RuntimeException("Redis down"));
            // Use a valid-looking 3-part JWT
            String rawToken = buildRawToken("jti-error");

            assertThat(service.isBlacklisted(rawToken)).isFalse();
        }

        @Test
        @DisplayName("Returns false for malformed token string")
        void shouldReturnFalseForMalformedToken() {
            when(redisTemplate.hasKey(any())).thenReturn(false);
            assertThat(service.isBlacklisted("not-a-jwt")).isFalse();
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private Jwt buildJwt(String jti, Instant expiry) {
        return buildJwt(jti, Instant.now(), expiry);
    }

    private Jwt buildJwt(String jti, Instant issuedAt, Instant expiry) {
        return Jwt.withTokenValue("mock-token")
                .header("alg", "RS256")
                .claim("sub", "user-123")
                .claim("jti", jti)
                .issuer("https://keycloak.test/realms/rasteronelab")
                .issuedAt(issuedAt)
                .expiresAt(expiry)
                .build();
    }

    private Jwt buildJwtNoJti(Instant expiry) {
        return Jwt.withTokenValue("mock-token-no-jti")
                .header("alg", "RS256")
                .claim("sub", "user-456")
                .issuer("https://keycloak.test/realms/rasteronelab")
                .issuedAt(Instant.now())
                .expiresAt(expiry)
                .build();
    }

    /**
     * Builds a minimal 3-part raw token string with the given jti in the payload.
     * The signature part is a placeholder — we only need the payload to be parseable.
     */
    private String buildRawToken(String jti) {
        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"RS256\"}".getBytes());
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(("{\"jti\":\"" + jti + "\",\"sub\":\"user\"}").getBytes());
        return header + "." + payload + ".signature";
    }
}
