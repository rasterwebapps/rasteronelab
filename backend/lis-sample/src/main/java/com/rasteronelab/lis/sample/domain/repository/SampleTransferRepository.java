package com.rasteronelab.lis.sample.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.sample.domain.model.SampleTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleTransferRepository extends BranchAwareRepository<SampleTransfer> {

    List<SampleTransfer> findAllBySampleIdOrderByTransferredAtDesc(UUID sampleId);

    Page<SampleTransfer> findAllBySourceBranchIdAndIsDeletedFalse(UUID sourceBranchId, Pageable pageable);

    Optional<SampleTransfer> findByIdAndIsDeletedFalse(UUID id);
}
