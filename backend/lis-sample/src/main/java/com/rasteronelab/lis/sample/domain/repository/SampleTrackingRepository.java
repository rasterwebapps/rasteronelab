package com.rasteronelab.lis.sample.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.sample.domain.model.SampleTracking;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SampleTrackingRepository extends BranchAwareRepository<SampleTracking> {

    List<SampleTracking> findAllBySampleIdOrderByEventTimeAsc(UUID sampleId);
}
