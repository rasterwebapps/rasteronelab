package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.ReferenceRangeRequest;
import com.rasteronelab.lis.admin.api.dto.ReferenceRangeResponse;
import com.rasteronelab.lis.admin.domain.model.ReferenceRange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for ReferenceRange entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface ReferenceRangeMapper {

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
    ReferenceRange toEntity(ReferenceRangeRequest request);

    @Mapping(source = "parameter.name", target = "parameterName")
    ReferenceRangeResponse toResponse(ReferenceRange referenceRange);

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
    void updateEntity(ReferenceRangeRequest request, @MappingTarget ReferenceRange referenceRange);
}
