package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * TAT (Turnaround Time) Configuration entity (L3 branch-level).
 * Defines expected turnaround times for tests/departments by urgency level.
 */
@Entity
@Table(name = "tat_configuration")
public class TATConfiguration extends BaseEntity {

    @Column(name = "test_id", insertable = false, updatable = false)
    private UUID testId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private TestMaster test;

    @Column(name = "department_id", insertable = false, updatable = false)
    private UUID departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "routine_hours", nullable = false)
    private Integer routineHours;

    @Column(name = "stat_hours")
    private Integer statHours;

    @Column(name = "critical_hours")
    private Integer criticalHours;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public UUID getTestId() {
        return this.testId;
    }

    public TestMaster getTest() {
        return this.test;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Integer getRoutineHours() {
        return this.routineHours;
    }

    public Integer getStatHours() {
        return this.statHours;
    }

    public Integer getCriticalHours() {
        return this.criticalHours;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTest(TestMaster test) {
        this.test = test;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setRoutineHours(Integer routineHours) {
        this.routineHours = routineHours;
    }

    public void setStatHours(Integer statHours) {
        this.statHours = statHours;
    }

    public void setCriticalHours(Integer criticalHours) {
        this.criticalHours = criticalHours;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
