package com.rasteronelab.lis.result.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.result.domain.model.ResultValue;

import java.util.List;
import java.util.UUID;

public interface ResultValueRepository extends BranchAwareRepository<ResultValue> {

    List<ResultValue> findAllByTestResultIdAndBranchIdAndIsDeletedFalse(UUID testResultId, UUID branchId);
}
