package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Role entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents a user role with feature-action permissions within a branch.
 */
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "description")
    private String description;

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getRoleName() {
        return this.roleName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
