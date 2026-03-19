package com.rasteronelab.lis.sample.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.event.SampleCollectedEvent;
import com.rasteronelab.lis.core.event.SampleRejectedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.core.util.BarcodeGeneratorUtil;
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
import com.rasteronelab.lis.sample.domain.model.CollectionSite;
import com.rasteronelab.lis.sample.domain.model.RejectionReason;
import com.rasteronelab.lis.sample.domain.model.Sample;
import com.rasteronelab.lis.sample.domain.model.SampleStatus;
import com.rasteronelab.lis.sample.domain.model.SampleTracking;
import com.rasteronelab.lis.sample.domain.model.SampleTransfer;
import com.rasteronelab.lis.sample.domain.model.TransferStatus;
import com.rasteronelab.lis.sample.domain.model.TubeType;
import com.rasteronelab.lis.sample.domain.repository.SampleRepository;
import com.rasteronelab.lis.sample.domain.repository.SampleTrackingRepository;
import com.rasteronelab.lis.sample.domain.repository.SampleTransferRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Core service for sample lifecycle management (LIS-055 to LIS-061).
 * Handles: collection, state machine, receiving, rejection, aliquoting,
 * storage, inter-branch transfer, and worklists.
 */
@Service
@Transactional
public class SampleService {

    private final SampleRepository sampleRepository;
    private final SampleTrackingRepository sampleTrackingRepository;
    private final SampleTransferRepository sampleTransferRepository;
    private final SampleMapper sampleMapper;
    private final SampleTrackingMapper sampleTrackingMapper;
    private final SampleTransferMapper sampleTransferMapper;
    private final ApplicationEventPublisher eventPublisher;

    public SampleService(SampleRepository sampleRepository,
                         SampleTrackingRepository sampleTrackingRepository,
                         SampleTransferRepository sampleTransferRepository,
                         SampleMapper sampleMapper,
                         SampleTrackingMapper sampleTrackingMapper,
                         SampleTransferMapper sampleTransferMapper,
                         ApplicationEventPublisher eventPublisher) {
        this.sampleRepository = sampleRepository;
        this.sampleTrackingRepository = sampleTrackingRepository;
        this.sampleTransferRepository = sampleTransferRepository;
        this.sampleMapper = sampleMapper;
        this.sampleTrackingMapper = sampleTrackingMapper;
        this.sampleTransferMapper = sampleTransferMapper;
        this.eventPublisher = eventPublisher;
    }

    // ── LIS-055: Sample Collection ────────────────────────────────────────────

    /**
     * Records collection of one or more tubes for an order.
     * Generates a unique sample barcode for each tube.
     * Publishes SampleCollectedEvent after all tubes are saved.
     */
    public List<SampleResponse> collectSamples(SampleCollectRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        List<Sample> collected = new ArrayList<>();

        for (SampleTubeRequest tube : request.getTubes()) {
            Sample sample = new Sample();
            sample.setBranchId(branchId);
            sample.setOrderId(request.getOrderId());
            sample.setPatientId(request.getPatientId());
            sample.setStatus(SampleStatus.COLLECTED);
            sample.setHomeCollection(request.getHomeCollection() != null && request.getHomeCollection());
            sample.setNotes(request.getNotes());

            sample.setTubeType(TubeType.valueOf(tube.getTubeType()));
            sample.setSampleType(tube.getSampleType());
            sample.setCollectedBy(tube.getCollectedBy());
            sample.setCollectedAt(tube.getCollectedAt() != null ? tube.getCollectedAt() : LocalDateTime.now());
            sample.setQuantity(tube.getQuantity());
            sample.setUnit(tube.getUnit());

            if (tube.getCollectionSite() != null) {
                sample.setCollectionSite(CollectionSite.valueOf(tube.getCollectionSite()));
            }

            long sequence = sampleRepository.countByBranchIdAndIsDeletedFalse(branchId) + collected.size() + 1;
            sample.setSampleBarcode(BarcodeGeneratorUtil.generateSampleBarcode(sequence));

            Sample saved = sampleRepository.save(sample);
            recordTracking(saved.getId(), branchId, SampleStatus.COLLECTED, tube.getCollectedBy(), "Sample collected");
            collected.add(saved);
        }

        // Publish event so order module can update order status to SAMPLE_COLLECTED
        if (!collected.isEmpty()) {
            Sample first = collected.get(0);
            eventPublisher.publishEvent(new SampleCollectedEvent(
                    first.getId(), request.getOrderId(), request.getPatientId(), branchId));
        }

        return collected.stream().map(sampleMapper::toResponse).collect(Collectors.toList());
    }

    // ── LIS-057: Receiving ────────────────────────────────────────────────────

    /**
     * Receives (accepts) a sample at lab reception. TAT clock starts here.
     * Validates sample is in COLLECTED status before transitioning to RECEIVED.
     */
    public SampleResponse receiveSample(UUID id, SampleReceiveRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        if (sample.getStatus() != SampleStatus.COLLECTED) {
            throw new BusinessRuleException("SMP_INVALID_STATUS",
                    "Sample must be in COLLECTED status to receive. Current: " + sample.getStatus());
        }

        sample.setStatus(SampleStatus.RECEIVED);
        sample.setReceivedBy(request.getReceivedBy());
        sample.setReceivedAt(request.getReceivedAt() != null ? request.getReceivedAt() : LocalDateTime.now());
        sample.setTatStartedAt(sample.getReceivedAt());
        sample.setTemperature(request.getTemperature());
        if (request.getNotes() != null) {
            sample.setNotes(request.getNotes());
        }

        Sample saved = sampleRepository.save(sample);
        recordTracking(saved.getId(), branchId, SampleStatus.RECEIVED, request.getReceivedBy(), "Sample received at lab");
        return sampleMapper.toResponse(saved);
    }

    /**
     * Rejects a sample with a mandatory reason.
     * Publishes SampleRejectedEvent if recollection is required.
     */
    public SampleResponse rejectSample(UUID id, SampleRejectRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        if (sample.getStatus() != SampleStatus.COLLECTED && sample.getStatus() != SampleStatus.RECEIVED) {
            throw new BusinessRuleException("SMP_INVALID_STATUS",
                    "Sample cannot be rejected in status: " + sample.getStatus());
        }

        sample.setStatus(SampleStatus.REJECTED);
        sample.setRejectionReason(RejectionReason.valueOf(request.getRejectionReason()));
        sample.setRejectionComment(request.getComments());
        sample.setRecollectionRequired(request.getRecollectionRequired() != null && request.getRecollectionRequired());
        sample.setRejectedBy(request.getRejectedBy());
        sample.setRejectedAt(LocalDateTime.now());

        Sample saved = sampleRepository.save(sample);
        recordTracking(saved.getId(), branchId, SampleStatus.REJECTED, request.getRejectedBy(),
                "Rejected: " + request.getRejectionReason());

        eventPublisher.publishEvent(new SampleRejectedEvent(
                saved.getId(), saved.getOrderId(), saved.getPatientId(), branchId,
                request.getRejectionReason(),
                saved.getRecollectionRequired() != null && saved.getRecollectionRequired()));

        return sampleMapper.toResponse(saved);
    }

    /**
     * Accepts a received sample (status RECEIVED → ACCEPTED).
     */
    public SampleResponse acceptSample(UUID id, UUID acceptedBy) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        if (sample.getStatus() != SampleStatus.RECEIVED) {
            throw new BusinessRuleException("SMP_INVALID_STATUS",
                    "Sample must be in RECEIVED status to accept. Current: " + sample.getStatus());
        }

        sample.setStatus(SampleStatus.ACCEPTED);
        Sample saved = sampleRepository.save(sample);
        recordTracking(saved.getId(), branchId, SampleStatus.ACCEPTED, acceptedBy, "Sample accepted");
        return sampleMapper.toResponse(saved);
    }

    // ── LIS-058: Aliquoting ───────────────────────────────────────────────────

    /**
     * Aliquots an accepted sample into multiple child samples.
     * Parent sample status → ALIQUOTED, child samples created with ACCEPTED status.
     */
    public List<SampleResponse> aliquotSample(UUID parentId, SampleAliquotRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample parent = findByIdAndBranch(parentId, branchId);

        if (parent.getStatus() != SampleStatus.ACCEPTED) {
            throw new BusinessRuleException("SMP_INVALID_STATUS",
                    "Sample must be ACCEPTED before aliquoting. Current: " + parent.getStatus());
        }

        parent.setStatus(SampleStatus.ALIQUOTED);
        sampleRepository.save(parent);
        recordTracking(parent.getId(), branchId, SampleStatus.ALIQUOTED, null, "Aliquoted into child samples");

        List<Sample> children = new ArrayList<>();
        int seq = 1;
        for (AliquotRequest aliquot : request.getAliquots()) {
            Sample child = new Sample();
            child.setBranchId(branchId);
            child.setOrderId(parent.getOrderId());
            child.setPatientId(parent.getPatientId());
            child.setTubeType(parent.getTubeType());
            child.setSampleType(parent.getSampleType());
            child.setCollectedBy(parent.getCollectedBy());
            child.setCollectedAt(parent.getCollectedAt());
            child.setStatus(SampleStatus.ACCEPTED);
            child.setParentSampleId(parent.getId());
            child.setAliquotSequence(seq);

            String aliquotSuffix = String.valueOf((char) ('A' + seq - 1));
            child.setAliquotLabel(aliquotSuffix);
            child.setSampleBarcode(parent.getSampleBarcode() + "-" + aliquotSuffix);
            child.setQuantity(aliquot.getVolume());
            child.setUnit(aliquot.getUnit() != null ? aliquot.getUnit() : parent.getUnit());
            child.setDepartmentId(aliquot.getDepartmentId());
            child.setReceivedAt(parent.getReceivedAt());
            child.setTatStartedAt(parent.getTatStartedAt());

            Sample savedChild = sampleRepository.save(child);
            recordTracking(savedChild.getId(), branchId, SampleStatus.ACCEPTED, null,
                    "Aliquot " + aliquotSuffix + " created from parent " + parent.getSampleBarcode());
            children.add(savedChild);
            seq++;
        }

        return children.stream().map(sampleMapper::toResponse).collect(Collectors.toList());
    }

    // ── LIS-059: Storage ──────────────────────────────────────────────────────

    /**
     * Records storage location for a completed/processed sample.
     */
    public SampleResponse storeSample(UUID id, SampleStorageRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        sample.setStorageRack(request.getStorageRack());
        sample.setStorageShelf(request.getStorageShelf());
        sample.setStoragePosition(request.getStoragePosition());
        sample.setStoredAt(LocalDateTime.now());
        sample.setDisposalDate(request.getDisposalDate());
        sample.setStatus(SampleStatus.STORED);

        Sample saved = sampleRepository.save(sample);
        recordTracking(saved.getId(), branchId, SampleStatus.STORED, null,
                "Stored at " + request.getStorageRack() + "/" + request.getStorageShelf() + "/" + request.getStoragePosition());
        return sampleMapper.toResponse(saved);
    }

    /**
     * Marks a stored sample as disposed.
     */
    public SampleResponse disposeSample(UUID id, UUID disposedBy) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        if (sample.getStatus() != SampleStatus.STORED && sample.getStatus() != SampleStatus.COMPLETED) {
            throw new BusinessRuleException("SMP_INVALID_STATUS",
                    "Sample must be STORED or COMPLETED to dispose. Current: " + sample.getStatus());
        }

        sample.setStatus(SampleStatus.DISPOSED);
        Sample saved = sampleRepository.save(sample);
        recordTracking(saved.getId(), branchId, SampleStatus.DISPOSED, disposedBy, "Sample disposed");
        return sampleMapper.toResponse(saved);
    }

    // ── LIS-059: Tracking ─────────────────────────────────────────────────────

    /**
     * Returns the full timeline of state changes for a sample.
     */
    @Transactional(readOnly = true)
    public List<SampleTrackingResponse> getTracking(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        findByIdAndBranch(id, branchId); // validate access
        return sampleTrackingRepository.findAllBySampleIdOrderByEventTimeAsc(id)
                .stream().map(sampleTrackingMapper::toResponse).collect(Collectors.toList());
    }

    // ── LIS-060: Inter-Branch Transfer ────────────────────────────────────────

    /**
     * Initiates an inter-branch sample transfer.
     * Only BRANCH_ADMIN+ should call this endpoint (enforced at controller level).
     */
    public SampleTransferResponse transferSample(UUID id, SampleTransferRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Sample sample = findByIdAndBranch(id, branchId);

        SampleTransfer transfer = new SampleTransfer();
        transfer.setBranchId(branchId);
        transfer.setSampleId(sample.getId());
        transfer.setSourceBranchId(branchId);
        transfer.setDestinationBranchId(request.getDestinationBranchId());
        transfer.setTransferredBy(request.getTransferredBy());
        transfer.setTransferredAt(LocalDateTime.now());
        transfer.setReason(request.getReason());
        transfer.setNotes(request.getNotes());
        transfer.setStatus(TransferStatus.IN_TRANSIT);

        SampleTransfer saved = sampleTransferRepository.save(transfer);
        return sampleTransferMapper.toResponse(saved);
    }

    /**
     * Confirms receipt of a transferred sample at the destination branch.
     */
    public SampleTransferResponse confirmTransferReceived(UUID transferId, UUID receivedBy) {
        SampleTransfer transfer = sampleTransferRepository.findByIdAndIsDeletedFalse(transferId)
                .orElseThrow(() -> new NotFoundException("SampleTransfer", transferId));

        transfer.setStatus(TransferStatus.RECEIVED_AT_DEST);
        transfer.setReceivedAt(LocalDateTime.now());
        transfer.setReceivedBy(receivedBy);

        return sampleTransferMapper.toResponse(sampleTransferRepository.save(transfer));
    }

    // ── LIS-061: Worklists ────────────────────────────────────────────────────

    /**
     * Pending collection: orders with PAID status (not yet sample collected).
     * Returns samples in COLLECTED status for this branch (already collected but not received).
     */
    @Transactional(readOnly = true)
    public Page<SampleResponse> getPendingReceipt(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return sampleRepository.findAllByStatusAndBranchIdAndIsDeletedFalse(SampleStatus.COLLECTED, branchId, pageable)
                .map(sampleMapper::toResponse);
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public SampleResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return sampleMapper.toResponse(findByIdAndBranch(id, branchId));
    }

    @Transactional(readOnly = true)
    public Page<SampleResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return sampleRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(sampleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SampleResponse> getByOrder(UUID orderId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return sampleRepository.findAllByOrderIdAndBranchIdAndIsDeletedFalse(orderId, branchId)
                .stream().map(sampleMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SampleResponse> getByStatus(SampleStatus status, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return sampleRepository.findAllByStatusAndBranchIdAndIsDeletedFalse(status, branchId, pageable)
                .map(sampleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SampleResponse getByBarcode(String barcode) {
        Sample sample = sampleRepository.findBySampleBarcodeAndIsDeletedFalse(barcode)
                .orElseThrow(() -> new NotFoundException("Sample barcode", barcode));
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!sample.getBranchId().equals(branchId)) {
            throw new NotFoundException("Sample", sample.getId());
        }
        return sampleMapper.toResponse(sample);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Sample findByIdAndBranch(UUID id, UUID branchId) {
        return sampleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Sample", id));
    }

    private void recordTracking(UUID sampleId, UUID branchId, SampleStatus status, UUID performedBy, String comments) {
        SampleTracking tracking = new SampleTracking();
        tracking.setBranchId(branchId);
        tracking.setSampleId(sampleId);
        tracking.setStatus(status);
        tracking.setEventTime(LocalDateTime.now());
        tracking.setPerformedBy(performedBy);
        tracking.setComments(comments);
        sampleTrackingRepository.save(tracking);
    }
}
