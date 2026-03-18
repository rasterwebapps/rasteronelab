package com.rasteronelab.lis.admin.api.dto;

import com.rasteronelab.lis.admin.domain.model.BranchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Branch entity.
 * Includes all entity fields plus organizationName for display.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {

    private UUID id;
    private UUID orgId;
    private String organizationName;
    private String name;
    private String code;
    private String displayName;
    private BranchType branchType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String phone;
    private String email;
    private String nablNumber;
    private String licenseNumber;
    private String headerText;
    private String footerText;
    private String logoUrl;
    private String reportHeaderUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
