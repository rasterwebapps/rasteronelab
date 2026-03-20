package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.CreditAccountRequest;
import com.rasteronelab.lis.billing.api.dto.CreditAccountResponse;
import com.rasteronelab.lis.billing.api.mapper.CreditAccountMapper;
import com.rasteronelab.lis.billing.domain.model.CreditAccount;
import com.rasteronelab.lis.billing.domain.repository.CreditAccountRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service for CreditAccount CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final CreditAccountMapper creditAccountMapper;

    public CreditAccountService(CreditAccountRepository creditAccountRepository,
                                CreditAccountMapper creditAccountMapper) {
        this.creditAccountRepository = creditAccountRepository;
        this.creditAccountMapper = creditAccountMapper;
    }

    public CreditAccountResponse create(CreditAccountRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (creditAccountRepository.existsByAccountNameAndBranchIdAndIsDeletedFalse(
                request.getAccountName(), branchId)) {
            throw new DuplicateResourceException("CreditAccount", "accountName", request.getAccountName());
        }

        CreditAccount account = creditAccountMapper.toEntity(request);
        account.setBranchId(branchId);
        account.setIsActive(true);
        account.setCurrentBalance(BigDecimal.ZERO);

        CreditAccount saved = creditAccountRepository.save(account);
        return creditAccountMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CreditAccountResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        CreditAccount account = creditAccountRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CreditAccount", id));
        return creditAccountMapper.toResponse(account);
    }

    @Transactional(readOnly = true)
    public Page<CreditAccountResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return creditAccountRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(creditAccountMapper::toResponse);
    }

    public CreditAccountResponse update(UUID id, CreditAccountRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        CreditAccount account = creditAccountRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CreditAccount", id));

        creditAccountMapper.updateEntity(request, account);
        CreditAccount saved = creditAccountRepository.save(account);
        return creditAccountMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        CreditAccount account = creditAccountRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CreditAccount", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        account.softDelete(currentUser);
        creditAccountRepository.save(account);
    }
}
