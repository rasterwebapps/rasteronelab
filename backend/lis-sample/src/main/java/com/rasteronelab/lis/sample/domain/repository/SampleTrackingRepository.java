package com.rasteronelab.lis.sample.domain.repository;

import com.rasteronelab.lis.sample.domain.model.SampleTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SampleTrackingRepository extends JpaRepository<SampleTracking, UUID> {

    List<SampleTracking> findAllBySampleIdOrderByEventTimeAsc(UUID sampleId);
}
