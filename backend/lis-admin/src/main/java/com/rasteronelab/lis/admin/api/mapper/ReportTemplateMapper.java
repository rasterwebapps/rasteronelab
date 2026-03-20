package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.ReportTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.ReportTemplateResponse;
import com.rasteronelab.lis.admin.domain.model.ReportTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for ReportTemplate entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface ReportTemplateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ReportTemplate toEntity(ReportTemplateRequest request);

    ReportTemplateResponse toResponse(ReportTemplate reportTemplate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(ReportTemplateRequest request, @MappingTarget ReportTemplate reportTemplate);
}
