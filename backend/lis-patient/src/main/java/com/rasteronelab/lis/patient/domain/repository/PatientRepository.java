package com.rasteronelab.lis.patient.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.patient.domain.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Patient entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface PatientRepository extends BranchAwareRepository<Patient> {

    Optional<Patient> findByUhidAndBranchIdAndIsDeletedFalse(String uhid, UUID branchId);

    List<Patient> findByPhoneAndBranchIdAndIsDeletedFalse(String phone, UUID branchId);

    Page<Patient> findAllByBranchIdAndIsDeletedFalseAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            UUID branchId, String firstName, String lastName, Pageable pageable);

    boolean existsByUhidAndBranchIdAndIsDeletedFalse(String uhid, UUID branchId);

    /**
     * Multi-criteria patient search: matches UHID, phone, first name, or last name.
     * All results are scoped to the given branch and exclude soft-deleted records.
     */
    @Query("SELECT p FROM Patient p WHERE p.branchId = :branchId AND p.isDeleted = false " +
           "AND (LOWER(p.uhid) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Patient> searchPatients(@Param("branchId") UUID branchId,
                                 @Param("searchTerm") String searchTerm,
                                 Pageable pageable);

    /**
     * Find potential duplicate patients by matching first name, last name, phone, or date of birth.
     */
    @Query("SELECT p FROM Patient p WHERE p.branchId = :branchId AND p.isDeleted = false " +
           "AND ((LOWER(p.firstName) = LOWER(:firstName) AND LOWER(p.lastName) = LOWER(:lastName)) " +
           "OR (p.phone = :phone) " +
           "OR (p.dateOfBirth = :dob AND LOWER(p.firstName) = LOWER(:firstName)))")
    List<Patient> findDuplicates(@Param("branchId") UUID branchId,
                                 @Param("firstName") String firstName,
                                 @Param("lastName") String lastName,
                                 @Param("phone") String phone,
                                 @Param("dob") LocalDate dob);
}
