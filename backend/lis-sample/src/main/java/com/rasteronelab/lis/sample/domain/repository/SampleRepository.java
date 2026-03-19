package com.rasteronelab.lis.sample.domain.repository;

import com.rasteronelab.lis.sample.domain.model.Sample;
import com.rasteronelab.lis.sample.domain.model.SampleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleRepository extends JpaRepository<Sample, UUID> {

    Page<Sample> findAllByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable);

    Optional<Sample> findByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId);

    Optional<Sample> findBySampleBarcodeAndIsDeletedFalse(String sampleBarcode);

    List<Sample> findAllByOrderIdAndBranchIdAndIsDeletedFalse(UUID orderId, UUID branchId);

    Page<Sample> findAllByStatusAndBranchIdAndIsDeletedFalse(SampleStatus status, UUID branchId, Pageable pageable);

    List<Sample> findAllByParentSampleIdAndIsDeletedFalse(UUID parentSampleId);

    long countByBranchIdAndIsDeletedFalse(UUID branchId);
}
