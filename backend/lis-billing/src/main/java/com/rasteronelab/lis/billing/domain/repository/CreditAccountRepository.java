package com.rasteronelab.lis.billing.domain.repository;

import com.rasteronelab.lis.billing.domain.model.CreditAccount;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for CreditAccount entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface CreditAccountRepository extends BranchAwareRepository<CreditAccount> {

    Optional<CreditAccount> findByAccountNameAndBranchIdAndIsDeletedFalse(String accountName, UUID branchId);

    boolean existsByAccountNameAndBranchIdAndIsDeletedFalse(String accountName, UUID branchId);
}
