package com.rasteronelab.lis.patient.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.patient.domain.model.PatientVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for PatientVisit entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface PatientVisitRepository extends BranchAwareRepository<PatientVisit> {

    Page<PatientVisit> findAllByPatientIdAndBranchIdAndIsDeletedFalse(
            UUID patientId, UUID branchId, Pageable pageable);

    boolean existsByVisitNumberAndBranchIdAndIsDeletedFalse(String visitNumber, UUID branchId);
}
