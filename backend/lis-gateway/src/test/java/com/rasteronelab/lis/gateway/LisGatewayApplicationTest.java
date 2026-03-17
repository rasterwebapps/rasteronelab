package com.rasteronelab.lis.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

/**
 * Verifies that the gateway application context loads successfully.
 */
@SpringBootTest
class LisGatewayApplicationTest {

    @MockBean
    @SuppressWarnings("unused")
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @Test
    @DisplayName("Application context loads successfully")
    void contextLoads() {
        // Context load verification — if this test passes,
        // all beans are wired correctly.
    }
}
