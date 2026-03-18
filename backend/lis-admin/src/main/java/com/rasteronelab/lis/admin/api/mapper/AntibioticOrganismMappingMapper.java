package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticOrganismMappingResponse;
import com.rasteronelab.lis.admin.domain.model.AntibioticOrganismMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for AntibioticOrganismMapping entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface AntibioticOrganismMappingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "antibiotic", ignore = true)
    @Mapping(target = "antibioticId", ignore = true)
    @Mapping(target = "microorganism", ignore = true)
    @Mapping(target = "microorganismId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AntibioticOrganismMapping toEntity(AntibioticOrganismMappingRequest request);

    @Mapping(source = "antibiotic.name", target = "antibioticName")
    @Mapping(source = "microorganism.name", target = "microorganismName")
    AntibioticOrganismMappingResponse toResponse(AntibioticOrganismMapping mapping);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "antibiotic", ignore = true)
    @Mapping(target = "antibioticId", ignore = true)
    @Mapping(target = "microorganism", ignore = true)
    @Mapping(target = "microorganismId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(AntibioticOrganismMappingRequest request, @MappingTarget AntibioticOrganismMapping mapping);
}
