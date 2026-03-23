package com.rasteronelab.lis.patient.api.mapper;

import com.rasteronelab.lis.patient.api.dto.PatientMergeAuditResponse;
import com.rasteronelab.lis.patient.domain.model.PatientMergeAudit;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for PatientMergeAudit entity → DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface PatientMergeAuditMapper {

    PatientMergeAuditResponse toResponse(PatientMergeAudit audit);
}
