package com.rasteronelab.lis.auth.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Extended JWT authentication token that carries LIS-specific claims
 * extracted from Keycloak: organizationId, branchIds, and employeeId.
 */
public class LisAuthenticationToken extends JwtAuthenticationToken {

    private final String organizationId;
    private final List<String> branchIds;
    private final String employeeId;

    public LisAuthenticationToken(Jwt jwt,
                                  Collection<? extends GrantedAuthority> authorities,
                                  String name,
                                  String organizationId,
                                  List<String> branchIds,
                                  String employeeId) {
        super(jwt, authorities, name);
        this.organizationId = organizationId;
        this.branchIds = branchIds != null
                ? Collections.unmodifiableList(branchIds)
                : Collections.emptyList();
        this.employeeId = employeeId;
    }

    public String getOrganizationId() {
        return this.organizationId;
    }

    public List<String> getBranchIds() {
        return this.branchIds;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

}
