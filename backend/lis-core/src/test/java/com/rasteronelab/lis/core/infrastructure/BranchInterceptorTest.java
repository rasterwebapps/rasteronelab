package com.rasteronelab.lis.core.infrastructure;

import com.rasteronelab.lis.core.common.exception.BranchAccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BranchInterceptor")
class BranchInterceptorTest {

    private BranchInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        interceptor = new BranchInterceptor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("preHandle should skip actuator endpoints")
    void preHandle_shouldSkipActuatorEndpoints() {
        request.setRequestURI("/actuator/health");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(BranchContextHolder.hasBranchContext()).isFalse();
    }

    @Test
    @DisplayName("preHandle should skip if no branch header is present")
    void preHandle_shouldSkipIfNoBranchHeader() {
        request.setRequestURI("/api/patients");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(BranchContextHolder.hasBranchContext()).isFalse();
    }

    @Test
    @DisplayName("preHandle should set branch context when valid branch and JWT access")
    void preHandle_shouldSetBranchContext_whenValidBranchAndJwtAccess() {
        UUID branchId = UUID.randomUUID();
        request.setRequestURI("/api/patients");
        request.addHeader("X-Branch-Id", branchId.toString());
        setUpJwtAuthentication(branchId.toString());

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        assertThat(BranchContextHolder.getCurrentBranchId()).isEqualTo(branchId);
    }

    @Test
    @DisplayName("preHandle should throw BranchAccessDeniedException when UUID is invalid")
    void preHandle_shouldThrowBranchAccessDenied_whenInvalidUuid() {
        request.setRequestURI("/api/patients");
        request.addHeader("X-Branch-Id", "not-a-valid-uuid");

        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(BranchAccessDeniedException.class)
                .hasMessageContaining("Invalid branch ID format");
    }

    @Test
    @DisplayName("preHandle should throw BranchAccessDeniedException when user lacks branch access")
    void preHandle_shouldThrowBranchAccessDenied_whenUserLacksBranchAccess() {
        UUID requestedBranch = UUID.randomUUID();
        UUID allowedBranch = UUID.randomUUID();
        request.setRequestURI("/api/patients");
        request.addHeader("X-Branch-Id", requestedBranch.toString());
        setUpJwtAuthentication(allowedBranch.toString());

        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(BranchAccessDeniedException.class);
    }

    @Test
    @DisplayName("afterCompletion should clear branch context")
    void afterCompletion_shouldClearBranchContext() {
        BranchContextHolder.setCurrentBranchId(UUID.randomUUID());

        interceptor.afterCompletion(request, response, new Object(), null);

        assertThat(BranchContextHolder.hasBranchContext()).isFalse();
    }

    private void setUpJwtAuthentication(String... branchIds) {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "RS256")
                .claim("sub", "test-user")
                .claim("branchIds", java.util.Arrays.asList(branchIds))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                jwt, List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
