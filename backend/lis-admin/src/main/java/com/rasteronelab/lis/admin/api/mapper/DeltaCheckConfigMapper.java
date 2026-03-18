package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigRequest;
import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigResponse;
import com.rasteronelab.lis.admin.domain.model.DeltaCheckConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for DeltaCheckConfig entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface DeltaCheckConfigMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "parameter", ignore = true)
    @Mapping(target = "parameterId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DeltaCheckConfig toEntity(DeltaCheckConfigRequest request);

    @Mapping(source = "parameter.name", target = "parameterName")
    DeltaCheckConfigResponse toResponse(DeltaCheckConfig deltaCheckConfig);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "parameter", ignore = true)
    @Mapping(target = "parameterId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(DeltaCheckConfigRequest request, @MappingTarget DeltaCheckConfig deltaCheckConfig);
}
