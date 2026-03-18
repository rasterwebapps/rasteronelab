package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.MicroorganismRequest;
import com.rasteronelab.lis.admin.api.dto.MicroorganismResponse;
import com.rasteronelab.lis.admin.domain.model.Microorganism;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Microorganism entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface MicroorganismMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Microorganism toEntity(MicroorganismRequest request);

    MicroorganismResponse toResponse(Microorganism microorganism);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(MicroorganismRequest request, @MappingTarget Microorganism microorganism);
}
