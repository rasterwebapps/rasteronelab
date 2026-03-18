package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.PanelTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for PanelTest mapping entity.
 * Queries for panel-test associations with soft-delete filtering.
 */
@Repository
public interface PanelTestRepository extends JpaRepository<PanelTest, UUID> {

    List<PanelTest> findAllByPanelIdAndIsDeletedFalse(UUID panelId);

    boolean existsByPanelIdAndTestIdAndIsDeletedFalse(UUID panelId, UUID testId);
}
