package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigRequest;
import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigResponse;
import com.rasteronelab.lis.admin.domain.model.CriticalValueConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for CriticalValueConfig entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface CriticalValueConfigMapper {

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
    CriticalValueConfig toEntity(CriticalValueConfigRequest request);

    @Mapping(source = "parameter.name", target = "parameterName")
    CriticalValueConfigResponse toResponse(CriticalValueConfig criticalValueConfig);

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
    void updateEntity(CriticalValueConfigRequest request, @MappingTarget CriticalValueConfig criticalValueConfig);
}
