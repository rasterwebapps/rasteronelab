package com.rasteronelab.lis.result.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.result.domain.model.ResultHistory;

import java.util.List;
import java.util.UUID;

public interface ResultHistoryRepository extends BranchAwareRepository<ResultHistory> {

    List<ResultHistory> findAllByTestResultIdAndBranchIdAndIsDeletedFalseOrderByPerformedAtDesc(
            UUID testResultId, UUID branchId);
}
