package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleRequest;
import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleResponse;
import com.rasteronelab.lis.admin.domain.model.AutoValidationRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for AutoValidationRule entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface AutoValidationRuleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "parameter", ignore = true)
    @Mapping(target = "parameterId", ignore = true)
    @Mapping(target = "test", ignore = true)
    @Mapping(target = "testId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AutoValidationRule toEntity(AutoValidationRuleRequest request);

    @Mapping(source = "parameter.name", target = "parameterName")
    @Mapping(source = "test.name", target = "testName")
    AutoValidationRuleResponse toResponse(AutoValidationRule autoValidationRule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "parameter", ignore = true)
    @Mapping(target = "parameterId", ignore = true)
    @Mapping(target = "test", ignore = true)
    @Mapping(target = "testId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(AutoValidationRuleRequest request, @MappingTarget AutoValidationRule autoValidationRule);
}
