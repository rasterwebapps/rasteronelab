package com.rasteronelab.lis.patient.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.patient.domain.model.PatientMergeAudit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for PatientMergeAudit entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface PatientMergeAuditRepository extends BranchAwareRepository<PatientMergeAudit> {

    List<PatientMergeAudit> findAllByPrimaryPatientIdAndBranchIdAndIsDeletedFalse(
            UUID primaryPatientId, UUID branchId);
}
