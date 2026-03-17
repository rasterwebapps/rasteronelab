package com.rasteronelab.lis.core.infrastructure;

import com.rasteronelab.lis.core.common.exception.BranchAccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BranchContextHolder")
class BranchContextHolderTest {

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    @Test
    @DisplayName("set and get should store and retrieve branchId")
    void setAndGet_shouldStoreAndRetrieveBranchId() {
        UUID branchId = UUID.randomUUID();

        BranchContextHolder.setCurrentBranchId(branchId);

        assertThat(BranchContextHolder.getCurrentBranchId()).isEqualTo(branchId);
    }

    @Test
    @DisplayName("getCurrentBranchId should throw BranchAccessDeniedException if not set")
    void getCurrentBranchId_shouldThrowIfNotSet() {
        assertThatThrownBy(BranchContextHolder::getCurrentBranchId)
                .isInstanceOf(BranchAccessDeniedException.class)
                .hasMessageContaining("No branch context set");
    }

    @Test
    @DisplayName("getCurrentBranchIdOrNull should return null if not set")
    void getCurrentBranchIdOrNull_shouldReturnNullIfNotSet() {
        assertThat(BranchContextHolder.getCurrentBranchIdOrNull()).isNull();
    }

    @Test
    @DisplayName("hasBranchContext should return false if not set")
    void hasBranchContext_shouldReturnFalseIfNotSet() {
        assertThat(BranchContextHolder.hasBranchContext()).isFalse();
    }

    @Test
    @DisplayName("hasBranchContext should return true if set")
    void hasBranchContext_shouldReturnTrueIfSet() {
        BranchContextHolder.setCurrentBranchId(UUID.randomUUID());

        assertThat(BranchContextHolder.hasBranchContext()).isTrue();
    }

    @Test
    @DisplayName("clear should remove branch context")
    void clear_shouldRemoveBranchContext() {
        BranchContextHolder.setCurrentBranchId(UUID.randomUUID());

        BranchContextHolder.clear();

        assertThat(BranchContextHolder.hasBranchContext()).isFalse();
        assertThat(BranchContextHolder.getCurrentBranchIdOrNull()).isNull();
    }

    @Test
    @DisplayName("setCurrentBranchId should throw IllegalArgumentException if null")
    void setCurrentBranchId_shouldThrowIfNull() {
        assertThatThrownBy(() -> BranchContextHolder.setCurrentBranchId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Branch ID cannot be null");
    }
}
