package com.rasteronelab.lis.report.domain.repository;

import com.rasteronelab.lis.report.domain.model.LabReport;
import com.rasteronelab.lis.report.domain.model.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, UUID> {

    Page<LabReport> findByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable);

    Optional<LabReport> findByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId);

    Optional<LabReport> findByBranchIdAndReportNumberAndIsDeletedFalse(UUID branchId, String reportNumber);

    List<LabReport> findByBranchIdAndOrderIdAndIsDeletedFalse(UUID branchId, UUID orderId);

    Page<LabReport> findByBranchIdAndPatientIdAndIsDeletedFalse(UUID branchId, UUID patientId, Pageable pageable);

    Page<LabReport> findByBranchIdAndReportStatusAndIsDeletedFalse(UUID branchId, ReportStatus status, Pageable pageable);

    long countByBranchIdAndIsDeletedFalseAndReportNumberStartingWith(UUID branchId, String prefix);

    long countByBranchIdAndIsDeletedFalse(UUID branchId);
}
