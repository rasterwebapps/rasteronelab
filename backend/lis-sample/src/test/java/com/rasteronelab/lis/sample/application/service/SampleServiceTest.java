package com.rasteronelab.lis.sample.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.event.SampleCollectedEvent;
import com.rasteronelab.lis.core.event.SampleReceivedEvent;
import com.rasteronelab.lis.core.event.SampleRejectedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.sample.api.dto.AliquotRequest;
import com.rasteronelab.lis.sample.api.dto.SampleAliquotRequest;
import com.rasteronelab.lis.sample.api.dto.SampleCollectRequest;
import com.rasteronelab.lis.sample.api.dto.SampleReceiveRequest;
import com.rasteronelab.lis.sample.api.dto.SampleRejectRequest;
import com.rasteronelab.lis.sample.api.dto.SampleResponse;
import com.rasteronelab.lis.sample.api.dto.SampleStorageRequest;
import com.rasteronelab.lis.sample.api.dto.SampleTrackingResponse;
import com.rasteronelab.lis.sample.api.dto.SampleTransferRequest;
import com.rasteronelab.lis.sample.api.dto.SampleTransferResponse;
import com.rasteronelab.lis.sample.api.dto.SampleTubeRequest;
import com.rasteronelab.lis.sample.api.mapper.SampleMapper;
import com.rasteronelab.lis.sample.api.mapper.SampleTrackingMapper;
import com.rasteronelab.lis.sample.api.mapper.SampleTransferMapper;
import com.rasteronelab.lis.sample.domain.model.Sample;
import com.rasteronelab.lis.sample.domain.model.SampleStatus;
import com.rasteronelab.lis.sample.domain.model.SampleTracking;
import com.rasteronelab.lis.sample.domain.model.SampleTransfer;
import com.rasteronelab.lis.sample.domain.model.TransferStatus;
import com.rasteronelab.lis.sample.domain.model.TubeType;
import com.rasteronelab.lis.sample.domain.repository.SampleRepository;
import com.rasteronelab.lis.sample.domain.repository.SampleTrackingRepository;
import com.rasteronelab.lis.sample.domain.repository.SampleTransferRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("SampleService")
@ExtendWith(MockitoExtension.class)
class SampleServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();
    private static final UUID ORDER_ID  = UUID.randomUUID();
    private static final UUID PATIENT_ID = UUID.randomUUID();
    private static final UUID COLLECTOR_ID = UUID.randomUUID();

    @Mock private SampleRepository sampleRepository;
    @Mock private SampleTrackingRepository sampleTrackingRepository;
    @Mock private SampleTransferRepository sampleTransferRepository;
    @Mock private SampleMapper sampleMapper;
    @Mock private SampleTrackingMapper sampleTrackingMapper;
    @Mock private SampleTransferMapper sampleTransferMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private SampleService sampleService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    // ── collectSamples ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("collectSamples should create one Sample per tube and publish SampleCollectedEvent")
    void collectSamples_shouldCreateSamplesAndPublishEvent() {
        SampleTubeRequest tube = new SampleTubeRequest();
        tube.setTubeType("EDTA");
        tube.setCollectedBy(COLLECTOR_ID);
        tube.setCollectedAt(LocalDateTime.now());

        SampleCollectRequest request = new SampleCollectRequest();
        request.setOrderId(ORDER_ID);
        request.setPatientId(PATIENT_ID);
        request.setTubes(List.of(tube));

        Sample savedSample = buildSample(SampleStatus.COLLECTED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(sampleRepository.save(any(Sample.class))).thenReturn(savedSample);
        when(sampleTrackingRepository.save(any(SampleTracking.class))).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(savedSample)).thenReturn(response);

        List<SampleResponse> result = sampleService.collectSamples(request);

        assertThat(result).hasSize(1).contains(response);
        verify(sampleRepository, times(1)).save(any(Sample.class));

        ArgumentCaptor<SampleCollectedEvent> eventCaptor = ArgumentCaptor.forClass(SampleCollectedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo(ORDER_ID);
        assertThat(eventCaptor.getValue().getBranchId()).isEqualTo(BRANCH_ID);
    }

    @Test
    @DisplayName("collectSamples with multiple tubes should create one sample per tube")
    void collectSamples_multipleTubes_shouldCreateOneSampleEach() {
        SampleTubeRequest tube1 = makeTubeRequest("EDTA");
        SampleTubeRequest tube2 = makeTubeRequest("RED");

        SampleCollectRequest request = new SampleCollectRequest();
        request.setOrderId(ORDER_ID);
        request.setPatientId(PATIENT_ID);
        request.setTubes(List.of(tube1, tube2));

        Sample s1 = buildSample(SampleStatus.COLLECTED);
        Sample s2 = buildSample(SampleStatus.COLLECTED);

        when(sampleRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(sampleRepository.save(any(Sample.class))).thenReturn(s1, s2);
        when(sampleTrackingRepository.save(any(SampleTracking.class))).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(any())).thenReturn(new SampleResponse());

        List<SampleResponse> result = sampleService.collectSamples(request);

        assertThat(result).hasSize(2);
        verify(sampleRepository, times(2)).save(any(Sample.class));
    }

    // ── receiveSample ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("receiveSample should transition COLLECTED → RECEIVED and set TAT clock")
    void receiveSample_shouldTransitionToReceived() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.COLLECTED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleRepository.save(sample)).thenReturn(sample);
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleReceiveRequest request = new SampleReceiveRequest();
        request.setReceivedBy(UUID.randomUUID());
        request.setReceivedAt(LocalDateTime.now());

        SampleResponse result = sampleService.receiveSample(id, request);

        assertThat(result).isEqualTo(response);
        assertThat(sample.getStatus()).isEqualTo(SampleStatus.RECEIVED);
        assertThat(sample.getTatStartedAt()).isNotNull();

        ArgumentCaptor<SampleReceivedEvent> eventCaptor = ArgumentCaptor.forClass(SampleReceivedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getSampleId()).isEqualTo(sample.getId());
        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo(ORDER_ID);
        assertThat(eventCaptor.getValue().getBranchId()).isEqualTo(BRANCH_ID);
    }

    @Test
    @DisplayName("receiveSample should throw BusinessRuleException when not in COLLECTED status")
    void receiveSample_shouldThrowWhenNotCollected() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.RECEIVED); // already received

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));

        SampleReceiveRequest request = new SampleReceiveRequest();
        request.setReceivedBy(UUID.randomUUID());
        request.setReceivedAt(LocalDateTime.now());

        assertThatThrownBy(() -> sampleService.receiveSample(id, request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("COLLECTED");
    }

    @Test
    @DisplayName("receiveSample should throw NotFoundException for unknown sample")
    void receiveSample_shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        SampleReceiveRequest request = new SampleReceiveRequest();
        request.setReceivedBy(UUID.randomUUID());
        request.setReceivedAt(LocalDateTime.now());

        assertThatThrownBy(() -> sampleService.receiveSample(id, request))
                .isInstanceOf(NotFoundException.class);
    }

    // ── rejectSample ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("rejectSample should transition to REJECTED and publish SampleRejectedEvent")
    void rejectSample_shouldRejectAndPublishEvent() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.COLLECTED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleRepository.save(sample)).thenReturn(sample);
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleRejectRequest request = new SampleRejectRequest();
        request.setRejectedBy(UUID.randomUUID());
        request.setRejectionReason("HEMOLYZED");
        request.setComments("Grossly haemolysed");
        request.setRecollectionRequired(true);

        SampleResponse result = sampleService.rejectSample(id, request);

        assertThat(result).isEqualTo(response);
        assertThat(sample.getStatus()).isEqualTo(SampleStatus.REJECTED);

        ArgumentCaptor<SampleRejectedEvent> captor = ArgumentCaptor.forClass(SampleRejectedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().getRejectionReason()).isEqualTo("HEMOLYZED");
        assertThat(captor.getValue().isRecollectionRequired()).isTrue();
    }

    @Test
    @DisplayName("rejectSample should throw BusinessRuleException when sample is already ACCEPTED")
    void rejectSample_shouldThrowWhenAlreadyAccepted() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.ACCEPTED);

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));

        SampleRejectRequest request = new SampleRejectRequest();
        request.setRejectedBy(UUID.randomUUID());
        request.setRejectionReason("HEMOLYZED");

        assertThatThrownBy(() -> sampleService.rejectSample(id, request))
                .isInstanceOf(BusinessRuleException.class);
    }

    // ── acceptSample ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("acceptSample should transition RECEIVED → ACCEPTED")
    void acceptSample_shouldTransitionToAccepted() {
        UUID id = UUID.randomUUID();
        UUID acceptedBy = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.RECEIVED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleRepository.save(sample)).thenReturn(sample);
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleResponse result = sampleService.acceptSample(id, acceptedBy);

        assertThat(result).isEqualTo(response);
        assertThat(sample.getStatus()).isEqualTo(SampleStatus.ACCEPTED);
    }

    // ── aliquotSample ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("aliquotSample should create child samples and set parent to ALIQUOTED")
    void aliquotSample_shouldCreateChildren() {
        UUID parentId = UUID.randomUUID();
        Sample parent = buildSample(SampleStatus.ACCEPTED);
        parent.setSampleBarcode("SMP-20260319-000001");
        parent.setTubeType(TubeType.EDTA);

        AliquotRequest a1 = new AliquotRequest();
        a1.setDepartmentId(UUID.randomUUID());
        a1.setVolume(BigDecimal.valueOf(2.0));

        AliquotRequest a2 = new AliquotRequest();
        a2.setDepartmentId(UUID.randomUUID());
        a2.setVolume(BigDecimal.valueOf(1.5));

        SampleAliquotRequest request = new SampleAliquotRequest();
        request.setAliquots(List.of(a1, a2));

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(parentId, BRANCH_ID)).thenReturn(Optional.of(parent));
        when(sampleRepository.save(any(Sample.class))).thenAnswer(inv -> inv.getArgument(0));
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(any())).thenReturn(new SampleResponse());

        List<SampleResponse> result = sampleService.aliquotSample(parentId, request);

        assertThat(result).hasSize(2);
        assertThat(parent.getStatus()).isEqualTo(SampleStatus.ALIQUOTED);
    }

    @Test
    @DisplayName("aliquotSample should throw when sample is not ACCEPTED")
    void aliquotSample_shouldThrowWhenNotAccepted() {
        UUID parentId = UUID.randomUUID();
        Sample parent = buildSample(SampleStatus.RECEIVED);

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(parentId, BRANCH_ID)).thenReturn(Optional.of(parent));

        SampleAliquotRequest request = new SampleAliquotRequest();
        request.setAliquots(List.of(new AliquotRequest()));

        assertThatThrownBy(() -> sampleService.aliquotSample(parentId, request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("ACCEPTED");
    }

    // ── storeSample ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("storeSample should record storage location and transition to STORED")
    void storeSample_shouldRecordStorageLocation() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.COMPLETED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleRepository.save(sample)).thenReturn(sample);
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleStorageRequest request = new SampleStorageRequest();
        request.setStorageRack("R1");
        request.setStorageShelf("S2");
        request.setStoragePosition("P3");

        SampleResponse result = sampleService.storeSample(id, request);

        assertThat(result).isEqualTo(response);
        assertThat(sample.getStatus()).isEqualTo(SampleStatus.STORED);
        assertThat(sample.getStorageRack()).isEqualTo("R1");
        assertThat(sample.getStorageShelf()).isEqualTo("S2");
        assertThat(sample.getStoragePosition()).isEqualTo("P3");
    }

    // ── getTracking ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getTracking should return ordered timeline for a sample")
    void getTracking_shouldReturnTimeline() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.RECEIVED);
        SampleTracking event = new SampleTracking();
        SampleTrackingResponse trackingResponse = new SampleTrackingResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleTrackingRepository.findAllBySampleIdOrderByEventTimeAsc(id)).thenReturn(List.of(event));
        when(sampleTrackingMapper.toResponse(event)).thenReturn(trackingResponse);

        List<SampleTrackingResponse> result = sampleService.getTracking(id);

        assertThat(result).hasSize(1).contains(trackingResponse);
    }

    // ── transferSample ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("transferSample should create a transfer record with IN_TRANSIT status")
    void transferSample_shouldCreateTransferRecord() {
        UUID id = UUID.randomUUID();
        UUID destBranch = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.ACCEPTED);
        SampleTransfer transfer = new SampleTransfer();
        transfer.setStatus(TransferStatus.IN_TRANSIT);
        SampleTransferResponse transferResponse = new SampleTransferResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleTransferRepository.save(any(SampleTransfer.class))).thenReturn(transfer);
        when(sampleTransferMapper.toResponse(transfer)).thenReturn(transferResponse);

        SampleTransferRequest request = new SampleTransferRequest();
        request.setDestinationBranchId(destBranch);
        request.setTransferredBy(UUID.randomUUID());
        request.setReason("Specialist test");

        SampleTransferResponse result = sampleService.transferSample(id, request);

        assertThat(result).isEqualTo(transferResponse);
        verify(sampleTransferRepository).save(any(SampleTransfer.class));
    }

    // ── getAll / getByOrder ────────────────────────────────────────────────────

    @Test
    @DisplayName("getAll should return paginated sample list")
    void getAll_shouldReturnPagedSamples() {
        Sample sample = buildSample(SampleStatus.COLLECTED);
        Page<Sample> page = new PageImpl<>(List.of(sample));

        when(sampleRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, PageRequest.of(0, 10))).thenReturn(page);
        when(sampleMapper.toResponse(sample)).thenReturn(new SampleResponse());

        Page<SampleResponse> result = sampleService.getAll(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("getByOrder should return all samples for an order")
    void getByOrder_shouldReturnSamplesForOrder() {
        Sample s1 = buildSample(SampleStatus.COLLECTED);
        Sample s2 = buildSample(SampleStatus.COLLECTED);

        when(sampleRepository.findAllByOrderIdAndBranchIdAndIsDeletedFalse(ORDER_ID, BRANCH_ID))
                .thenReturn(List.of(s1, s2));
        when(sampleMapper.toResponse(any())).thenReturn(new SampleResponse());

        List<SampleResponse> result = sampleService.getByOrder(ORDER_ID);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("getByBarcode should return sample for valid barcode in current branch")
    void getByBarcode_shouldReturnSampleForValidBarcode() {
        Sample sample = buildSample(SampleStatus.COLLECTED);
        sample.setSampleBarcode("SMP-20260319-000001");
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findBySampleBarcodeAndIsDeletedFalse("SMP-20260319-000001")).thenReturn(Optional.of(sample));
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleResponse result = sampleService.getByBarcode("SMP-20260319-000001");

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getByBarcode should throw NotFoundException for unknown barcode")
    void getByBarcode_shouldThrowForUnknownBarcode() {
        when(sampleRepository.findBySampleBarcodeAndIsDeletedFalse("INVALID")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sampleService.getByBarcode("INVALID"))
                .isInstanceOf(NotFoundException.class);
    }

    // ── disposeSample ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("disposeSample should transition STORED → DISPOSED")
    void disposeSample_shouldTransitionToDisposed() {
        UUID id = UUID.randomUUID();
        UUID disposedBy = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.STORED);
        SampleResponse response = new SampleResponse();

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));
        when(sampleRepository.save(sample)).thenReturn(sample);
        when(sampleTrackingRepository.save(any())).thenReturn(new SampleTracking());
        when(sampleMapper.toResponse(sample)).thenReturn(response);

        SampleResponse result = sampleService.disposeSample(id, disposedBy);

        assertThat(result).isEqualTo(response);
        assertThat(sample.getStatus()).isEqualTo(SampleStatus.DISPOSED);
    }

    @Test
    @DisplayName("disposeSample should throw when sample is COLLECTED (not stored)")
    void disposeSample_shouldThrowWhenNotStoredOrCompleted() {
        UUID id = UUID.randomUUID();
        Sample sample = buildSample(SampleStatus.COLLECTED);

        when(sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(sample));

        assertThatThrownBy(() -> sampleService.disposeSample(id, UUID.randomUUID()))
                .isInstanceOf(BusinessRuleException.class);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Sample buildSample(SampleStatus status) {
        Sample sample = new Sample();
        sample.setBranchId(BRANCH_ID);
        sample.setOrderId(ORDER_ID);
        sample.setPatientId(PATIENT_ID);
        sample.setStatus(status);
        sample.setTubeType(TubeType.EDTA);
        sample.setCollectedBy(COLLECTOR_ID);
        sample.setCollectedAt(LocalDateTime.now());
        sample.setSampleBarcode("SMP-20260319-000001");
        return sample;
    }

    private SampleTubeRequest makeTubeRequest(String tubeType) {
        SampleTubeRequest tube = new SampleTubeRequest();
        tube.setTubeType(tubeType);
        tube.setCollectedBy(COLLECTOR_ID);
        tube.setCollectedAt(LocalDateTime.now());
        return tube;
    }
}
