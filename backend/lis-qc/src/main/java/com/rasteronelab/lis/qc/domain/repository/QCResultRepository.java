package com.rasteronelab.lis.qc.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.qc.domain.model.QCResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface QCResultRepository extends BranchAwareRepository<QCResult> {

    Page<QCResult> findByBranchIdAndQcLotIdAndIsDeletedFalse(
            UUID branchId, UUID qcLotId, Pageable pageable);

    Page<QCResult> findByBranchIdAndQcLotIdAndPerformedAtBetweenAndIsDeletedFalse(
            UUID branchId, UUID qcLotId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<QCResult> findByBranchIdAndQcLotIdAndIsDeletedFalseOrderByPerformedAtDesc(
            UUID branchId, UUID qcLotId);
}
