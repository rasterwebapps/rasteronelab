package com.rasteronelab.lis.result.api.mapper;

import com.rasteronelab.lis.result.api.dto.TestResultCreateRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.domain.model.TestResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ResultValueMapper.class})
public interface TestResultMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "enteredBy", ignore = true)
    @Mapping(target = "enteredAt", ignore = true)
    @Mapping(target = "validatedBy", ignore = true)
    @Mapping(target = "validatedAt", ignore = true)
    @Mapping(target = "authorizedBy", ignore = true)
    @Mapping(target = "authorizedAt", ignore = true)
    @Mapping(target = "isCritical", ignore = true)
    @Mapping(target = "criticalAcknowledged", ignore = true)
    @Mapping(target = "criticalAcknowledgedBy", ignore = true)
    @Mapping(target = "criticalAcknowledgedAt", ignore = true)
    @Mapping(target = "hasDeltaCheckFailure", ignore = true)
    @Mapping(target = "isAmended", ignore = true)
    @Mapping(target = "amendmentReason", ignore = true)
    @Mapping(target = "amendedBy", ignore = true)
    @Mapping(target = "amendedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "resultValues", ignore = true)
    TestResult toEntity(TestResultCreateRequest request);

    @Mapping(target = "status", expression = "java(testResult.getStatus() != null ? testResult.getStatus().name() : null)")
    TestResultResponse toResponse(TestResult testResult);
}
