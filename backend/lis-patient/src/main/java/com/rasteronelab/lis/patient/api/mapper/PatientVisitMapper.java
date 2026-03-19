package com.rasteronelab.lis.patient.api.mapper;

import com.rasteronelab.lis.patient.api.dto.PatientVisitRequest;
import com.rasteronelab.lis.patient.api.dto.PatientVisitResponse;
import com.rasteronelab.lis.patient.domain.model.PatientVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for PatientVisit entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface PatientVisitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "visitNumber", ignore = true)
    @Mapping(target = "visitDate", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    PatientVisit toEntity(PatientVisitRequest request);

    PatientVisitResponse toResponse(PatientVisit visit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "visitNumber", ignore = true)
    @Mapping(target = "visitDate", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(PatientVisitRequest request, @MappingTarget PatientVisit visit);
}
