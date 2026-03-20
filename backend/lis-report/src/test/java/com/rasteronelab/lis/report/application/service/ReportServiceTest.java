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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ReportService")
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final UUID PATIENT_ID = UUID.randomUUID();
    private static final UUID REPORT_ID = UUID.randomUUID();

    @Mock
    private LabReportRepository reportRepository;

    @Mock
    private LabReportMapper reportMapper;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    // ── generateReport ────────────────────────────────────────────────────────

    @Test
    @DisplayName("generateReport should create report in GENERATED status")
    void generateReport_shouldCreateReport() {
        ReportGenerateRequest request = new ReportGenerateRequest();
        request.setOrderId(ORDER_ID);
        request.setPatientId(PATIENT_ID);
        request.setPatientName("John Doe");
        request.setReportType("INDIVIDUAL");

        LabReport savedReport = buildReport(ReportStatus.GENERATED);
        ReportResponse response = new ReportResponse();
        response.setId(savedReport.getId());
        response.setReportStatus("GENERATED");

        when(reportRepository.countByBranchIdAndIsDeletedFalseAndReportNumberStartingWith(eq(BRANCH_ID), anyString()))
                .thenReturn(0L);
        when(reportRepository.save(any(LabReport.class))).thenReturn(savedReport);
        when(reportMapper.toResponse(savedReport)).thenReturn(response);

        ReportResponse result = reportService.generateReport(request);

        assertThat(result).isNotNull();
        assertThat(result.getReportStatus()).isEqualTo("GENERATED");
        verify(reportRepository, times(1)).save(any(LabReport.class));
    }

    @Test
    @DisplayName("generateReport should generate report number in RPT-yyyyMMdd-XXXX format")
    void generateReport_shouldGenerateReportNumber() {
        ReportGenerateRequest request = new ReportGenerateRequest();
        request.setOrderId(ORDER_ID);
        request.setPatientId(PATIENT_ID);
        request.setPatientName("Jane Doe");

        LabReport savedReport = buildReport(ReportStatus.GENERATED);
        ReportResponse response = new ReportResponse();

        when(reportRepository.countByBranchIdAndIsDeletedFalseAndReportNumberStartingWith(eq(BRANCH_ID), anyString()))
                .thenReturn(5L);
        when(reportRepository.save(any(LabReport.class))).thenReturn(savedReport);
        when(reportMapper.toResponse(savedReport)).thenReturn(response);

        reportService.generateReport(request);

        ArgumentCaptor<LabReport> captor = ArgumentCaptor.forClass(LabReport.class);
        verify(reportRepository).save(captor.capture());
        LabReport captured = captor.getValue();

        assertThat(captured.getReportNumber()).startsWith("RPT-");
        assertThat(captured.getReportNumber()).matches("RPT-\\d{8}-\\d{4}");
        assertThat(captured.getReportNumber()).endsWith("-0006");
    }

    // ── getById ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById should return report when found")
    void getById_shouldReturnReport() {
        LabReport report = buildReport(ReportStatus.GENERATED);
        ReportResponse response = new ReportResponse();
        response.setId(REPORT_ID);

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(report));
        when(reportMapper.toResponse(report)).thenReturn(response);

        ReportResponse result = reportService.getById(REPORT_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(REPORT_ID);
    }

    @Test
    @DisplayName("getById should throw NotFoundException when not found")
    void getById_shouldThrowNotFound() {
        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.getById(REPORT_ID))
                .isInstanceOf(NotFoundException.class);
    }

    // ── signReport ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("signReport should transition GENERATED → SIGNED")
    void signReport_shouldTransitionToSigned() {
        LabReport report = buildReport(ReportStatus.GENERATED);
        ReportSignRequest request = new ReportSignRequest();
        request.setSignedBy("Dr. Smith");

        LabReport savedReport = buildReport(ReportStatus.SIGNED);
        savedReport.setSignedBy("Dr. Smith");
        ReportResponse response = new ReportResponse();
        response.setReportStatus("SIGNED");
        response.setSignedBy("Dr. Smith");

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(report));
        when(reportRepository.save(any(LabReport.class))).thenReturn(savedReport);
        when(reportMapper.toResponse(savedReport)).thenReturn(response);

        ReportResponse result = reportService.signReport(REPORT_ID, request);

        assertThat(result.getReportStatus()).isEqualTo("SIGNED");
        assertThat(result.getSignedBy()).isEqualTo("Dr. Smith");

        ArgumentCaptor<LabReport> captor = ArgumentCaptor.forClass(LabReport.class);
        verify(reportRepository).save(captor.capture());
        assertThat(captor.getValue().getReportStatus()).isEqualTo(ReportStatus.SIGNED);
        assertThat(captor.getValue().getSignedBy()).isEqualTo("Dr. Smith");
        assertThat(captor.getValue().getSignedAt()).isNotNull();
    }

    @Test
    @DisplayName("signReport should throw when report is not in GENERATED status")
    void signReport_shouldThrowWhenNotGenerated() {
        LabReport report = buildReport(ReportStatus.DRAFT);
        ReportSignRequest request = new ReportSignRequest();
        request.setSignedBy("Dr. Smith");

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(report));

        assertThatThrownBy(() -> reportService.signReport(REPORT_ID, request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("GENERATED");

        verify(reportRepository, never()).save(any(LabReport.class));
    }

    // ── deliverReport ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("deliverReport should transition SIGNED → DELIVERED")
    void deliverReport_shouldTransitionToDelivered() {
        LabReport report = buildReport(ReportStatus.SIGNED);
        ReportDeliverRequest request = new ReportDeliverRequest();
        request.setDeliveryChannel("EMAIL");

        LabReport savedReport = buildReport(ReportStatus.DELIVERED);
        savedReport.setDeliveryChannel("EMAIL");
        ReportResponse response = new ReportResponse();
        response.setReportStatus("DELIVERED");
        response.setDeliveryChannel("EMAIL");

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(report));
        when(reportRepository.save(any(LabReport.class))).thenReturn(savedReport);
        when(reportMapper.toResponse(savedReport)).thenReturn(response);

        ReportResponse result = reportService.deliverReport(REPORT_ID, request);

        assertThat(result.getReportStatus()).isEqualTo("DELIVERED");
        assertThat(result.getDeliveryChannel()).isEqualTo("EMAIL");

        ArgumentCaptor<LabReport> captor = ArgumentCaptor.forClass(LabReport.class);
        verify(reportRepository).save(captor.capture());
        assertThat(captor.getValue().getReportStatus()).isEqualTo(ReportStatus.DELIVERED);
        assertThat(captor.getValue().getDeliveredAt()).isNotNull();
    }

    // ── amendReport ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("amendReport should create new version and mark original as AMENDED")
    void amendReport_shouldCreateNewVersion() {
        LabReport original = buildReport(ReportStatus.SIGNED);
        original.setVersion(1);
        original.setReportNumber("RPT-20260319-0001");
        original.setPatientName("John Doe");
        original.setOrderId(ORDER_ID);
        original.setPatientId(PATIENT_ID);

        LabReport amendedReport = buildReport(ReportStatus.GENERATED);
        amendedReport.setVersion(2);
        amendedReport.setReportType(ReportType.AMENDED);
        ReportResponse response = new ReportResponse();
        response.setVersion(2);
        response.setReportType("AMENDED");
        response.setReportStatus("GENERATED");

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(original));
        when(reportRepository.save(any(LabReport.class)))
                .thenReturn(original)
                .thenReturn(amendedReport);
        when(reportMapper.toResponse(amendedReport)).thenReturn(response);

        ReportResponse result = reportService.amendReport(REPORT_ID, "Correction needed");

        assertThat(result.getVersion()).isEqualTo(2);
        assertThat(result.getReportType()).isEqualTo("AMENDED");

        // Verify original was saved (marked as AMENDED) and new version was saved
        verify(reportRepository, times(2)).save(any(LabReport.class));
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete should soft-delete the report")
    void delete_shouldSoftDelete() {
        LabReport report = buildReport(ReportStatus.GENERATED);

        when(reportRepository.findByIdAndBranchIdAndIsDeletedFalse(REPORT_ID, BRANCH_ID))
                .thenReturn(Optional.of(report));
        when(reportRepository.save(any(LabReport.class))).thenReturn(report);

        reportService.delete(REPORT_ID);

        ArgumentCaptor<LabReport> captor = ArgumentCaptor.forClass(LabReport.class);
        verify(reportRepository).save(captor.capture());
        assertThat(captor.getValue().getIsDeleted()).isTrue();
        assertThat(captor.getValue().getDeletedAt()).isNotNull();
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private LabReport buildReport(ReportStatus status) {
        LabReport report = new LabReport();
        report.setId(REPORT_ID);
        report.setBranchId(BRANCH_ID);
        report.setOrderId(ORDER_ID);
        report.setPatientId(PATIENT_ID);
        report.setPatientName("John Doe");
        report.setReportNumber("RPT-20260319-0001");
        report.setReportType(ReportType.INDIVIDUAL);
        report.setReportStatus(status);
        report.setVersion(1);
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy("SYSTEM");
        return report;
    }
}
