package com.rasteronelab.lis.report.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.report.api.dto.ReportDeliverRequest;
import com.rasteronelab.lis.report.api.dto.ReportGenerateRequest;
import com.rasteronelab.lis.report.api.dto.ReportResponse;
import com.rasteronelab.lis.report.api.dto.ReportSignRequest;
import com.rasteronelab.lis.report.api.mapper.LabReportMapper;
import com.rasteronelab.lis.report.domain.model.LabReport;
import com.rasteronelab.lis.report.domain.model.ReportStatus;
import com.rasteronelab.lis.report.domain.model.ReportType;
import com.rasteronelab.lis.report.domain.repository.LabReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Core service for lab report management.
 * Handles: generation, signing, delivery, amendment, and queries.
 */
@Service
@Transactional
public class ReportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final LabReportRepository reportRepository;
    private final LabReportMapper reportMapper;

    public ReportService(LabReportRepository reportRepository,
                         LabReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    /**
     * Generates a new lab report for a test order.
     * Creates report in GENERATED status with auto-generated report number (RPT-yyyyMMdd-XXXX).
     */
    public ReportResponse generateReport(ReportGenerateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        LabReport report = new LabReport();
        report.setBranchId(branchId);
        report.setOrderId(request.getOrderId());
        report.setPatientId(request.getPatientId());
        report.setPatientName(request.getPatientName());
        report.setReportType(ReportType.valueOf(
                request.getReportType() != null ? request.getReportType() : "INDIVIDUAL"));
        report.setReportStatus(ReportStatus.GENERATED);
        report.setDepartmentId(request.getDepartmentId());
        report.setDepartmentName(request.getDepartmentName());
        report.setTemplateId(request.getTemplateId());
        report.setNotes(request.getNotes());
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy("SYSTEM");
        report.setVersion(1);

        String reportNumber = generateReportNumber(branchId);
        report.setReportNumber(reportNumber);

        LabReport saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }

    /**
     * Gets a report by ID, scoped to the current branch.
     */
    @Transactional(readOnly = true)
    public ReportResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        LabReport report = findByIdAndBranch(id, branchId);
        return reportMapper.toResponse(report);
    }

    /**
     * Gets all reports for a given order.
     */
    @Transactional(readOnly = true)
    public List<ReportResponse> getByOrderId(UUID orderId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        List<LabReport> reports = reportRepository.findByBranchIdAndOrderIdAndIsDeletedFalse(branchId, orderId);
        return reports.stream().map(reportMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Gets paginated reports for a patient.
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getByPatientId(UUID patientId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return reportRepository.findByBranchIdAndPatientIdAndIsDeletedFalse(branchId, patientId, pageable)
                .map(reportMapper::toResponse);
    }

    /**
     * Gets paginated reports by status.
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getByStatus(ReportStatus status, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return reportRepository.findByBranchIdAndReportStatusAndIsDeletedFalse(branchId, status, pageable)
                .map(reportMapper::toResponse);
    }

    /**
     * Gets all reports for the current branch (paginated).
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return reportRepository.findByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(reportMapper::toResponse);
    }

    /**
     * Signs a report. Transitions GENERATED → SIGNED.
     */
    public ReportResponse signReport(UUID id, ReportSignRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        LabReport report = findByIdAndBranch(id, branchId);

        if (report.getReportStatus() != ReportStatus.GENERATED) {
            throw new BusinessRuleException("RPT_INVALID_STATUS",
                    "Report must be in GENERATED status to sign. Current: " + report.getReportStatus());
        }

        report.setReportStatus(ReportStatus.SIGNED);
        report.setSignedBy(request.getSignedBy());
        report.setSignedAt(LocalDateTime.now());
        if (request.getNotes() != null) {
            report.setNotes(request.getNotes());
        }

        LabReport saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }

    /**
     * Delivers a report. Transitions SIGNED → DELIVERED.
     */
    public ReportResponse deliverReport(UUID id, ReportDeliverRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        LabReport report = findByIdAndBranch(id, branchId);

        if (report.getReportStatus() != ReportStatus.SIGNED) {
            throw new BusinessRuleException("RPT_INVALID_STATUS",
                    "Report must be in SIGNED status to deliver. Current: " + report.getReportStatus());
        }

        report.setReportStatus(ReportStatus.DELIVERED);
        report.setDeliveredAt(LocalDateTime.now());
        report.setDeliveryChannel(request.getDeliveryChannel());

        LabReport saved = reportRepository.save(report);
        return reportMapper.toResponse(saved);
    }

    /**
     * Amends a report. Creates a new version by copying the original report,
     * incrementing the version, and marking the original as AMENDED.
     */
    public ReportResponse amendReport(UUID id, String reason) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        LabReport original = findByIdAndBranch(id, branchId);

        if (original.getReportStatus() != ReportStatus.SIGNED
                && original.getReportStatus() != ReportStatus.DELIVERED) {
            throw new BusinessRuleException("RPT_INVALID_STATUS",
                    "Report must be in SIGNED or DELIVERED status to amend. Current: " + original.getReportStatus());
        }

        // Mark original as AMENDED
        original.setReportStatus(ReportStatus.AMENDED);
        original.setAmendmentReason(reason);
        reportRepository.save(original);

        // Create new version
        LabReport amended = new LabReport();
        amended.setBranchId(branchId);
        amended.setOrderId(original.getOrderId());
        amended.setPatientId(original.getPatientId());
        amended.setPatientName(original.getPatientName());
        amended.setReportNumber(original.getReportNumber());
        amended.setReportType(ReportType.AMENDED);
        amended.setReportStatus(ReportStatus.GENERATED);
        amended.setDepartmentId(original.getDepartmentId());
        amended.setDepartmentName(original.getDepartmentName());
        amended.setTemplateId(original.getTemplateId());
        amended.setGeneratedAt(LocalDateTime.now());
        amended.setGeneratedBy("SYSTEM");
        amended.setVersion(original.getVersion() + 1);
        amended.setAmendmentReason(reason);
        amended.setNotes(original.getNotes());

        LabReport saved = reportRepository.save(amended);
        return reportMapper.toResponse(saved);
    }

    /**
     * Soft-deletes a report.
     */
    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        LabReport report = findByIdAndBranch(id, branchId);
        report.softDelete("SYSTEM");
        reportRepository.save(report);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private LabReport findByIdAndBranch(UUID id, UUID branchId) {
        return reportRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Report", id));
    }

    /**
     * Generates a unique report number in the format RPT-yyyyMMdd-XXXX.
     * Sequence is scoped to the current branch and date prefix.
     */
    String generateReportNumber(UUID branchId) {
        String datePrefix = "RPT-" + LocalDateTime.now().format(DATE_FORMAT) + "-";
        long count = reportRepository.countByBranchIdAndIsDeletedFalseAndReportNumberStartingWith(branchId, datePrefix);
        return datePrefix + String.format("%04d", count + 1);
    }
}
