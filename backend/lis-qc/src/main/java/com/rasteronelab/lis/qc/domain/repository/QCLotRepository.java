package com.rasteronelab.lis.qc.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.qc.domain.model.QCLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QCLotRepository extends BranchAwareRepository<QCLot> {

    Optional<QCLot> findByBranchIdAndLotNumberAndIsDeletedFalse(UUID branchId, String lotNumber);

    Page<QCLot> findByBranchIdAndTestIdAndIsActiveAndIsDeletedFalse(
            UUID branchId, UUID testId, boolean isActive, Pageable pageable);

    Page<QCLot> findByBranchIdAndDepartmentIdAndIsActiveAndIsDeletedFalse(
            UUID branchId, UUID departmentId, boolean isActive, Pageable pageable);
}
