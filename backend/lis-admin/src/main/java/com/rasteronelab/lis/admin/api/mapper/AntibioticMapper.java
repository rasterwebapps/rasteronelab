package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.AntibioticRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticResponse;
import com.rasteronelab.lis.admin.domain.model.Antibiotic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Antibiotic entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface AntibioticMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Antibiotic toEntity(AntibioticRequest request);

    AntibioticResponse toResponse(Antibiotic antibiotic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(AntibioticRequest request, @MappingTarget Antibiotic antibiotic);
}
