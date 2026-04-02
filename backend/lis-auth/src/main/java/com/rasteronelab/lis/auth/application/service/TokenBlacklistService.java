package com.rasteronelab.lis.auth.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

/**
 * Manages a Redis-backed blacklist of revoked JWT tokens.
 *
 * <p>On logout the full token value (raw JWT string) is stored in Redis with a TTL
 * equal to the token's remaining lifetime, so the entry auto-expires when the token
 * would have expired anyway. This prevents Redis from growing unboundedly while
 * ensuring revoked tokens cannot be reused until they naturally expire.</p>
 *
 * <p>Redis key format: {@code auth:blacklist:<jti>}</p>
 */
@Service
public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String KEY_PREFIX = "auth:blacklist:";

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Adds the given JWT to the blacklist, expiring the Redis entry when the
     * token itself expires so that memory is reclaimed automatically.
     *
     * @param jwt  the decoded JWT (carries expiry and jti claims)
     * @param rawToken the raw Bearer token string presented by the client
     */
    public void blacklist(Jwt jwt, String rawToken) {
        String jti = jwt.getId();
        String key = KEY_PREFIX + (jti != null ? jti : rawToken.hashCode());

        Duration ttl = computeTtl(jwt);
        if (ttl.isNegative() || ttl.isZero()) {
            log.debug("Token already expired, skipping blacklist entry for jti={}", jti);
            return;
        }

        redisTemplate.opsForValue().set(key, "revoked", ttl);
        log.info("Token blacklisted: jti={}, ttl={}s", jti, ttl.getSeconds());
    }

    /**
     * Returns {@code true} if the raw token value appears in the blacklist.
     *
     * <p>The check is done by re-deriving the key from the raw token, which requires
     * parsing the JTI. For performance we store the raw token hash as a fallback key
     * when JTI is absent.</p>
     *
     * @param rawToken raw Bearer token string from the Authorization header
     */
    public boolean isBlacklisted(String rawToken) {
        try {
            String jti = extractJtiFromRaw(rawToken);
            String key = KEY_PREFIX + (jti != null ? jti : rawToken.hashCode());
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.warn("Could not check blacklist for token: {}", e.getMessage());
            return false;
        }
    }

    private Duration computeTtl(Jwt jwt) {
        Instant expiry = jwt.getExpiresAt();
        if (expiry == null) {
            return Duration.ofHours(1);
        }
        return Duration.between(Instant.now(), expiry);
    }

    /**
     * Extracts the {@code jti} claim from a raw JWT string without full signature
     * verification (the token has already been verified by Spring Security at this point).
     */
    @SuppressWarnings("unchecked")
    private String extractJtiFromRaw(String rawToken) {
        try {
            String[] parts = rawToken.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            byte[] payloadBytes = java.util.Base64.getUrlDecoder()
                    .decode(padBase64(parts[1]));
            Map<String, Object> claims = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(payloadBytes, Map.class);
            Object jti = claims.get("jti");
            return jti != null ? jti.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String padBase64(String base64) {
        int remainder = base64.length() % 4;
        if (remainder == 0) return base64;
        return base64 + "=".repeat(4 - remainder);
    }
}
