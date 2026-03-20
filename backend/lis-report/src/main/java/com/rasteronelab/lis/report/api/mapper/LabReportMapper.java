package com.rasteronelab.lis.report.api.mapper;

import com.rasteronelab.lis.report.api.dto.ReportResponse;
import com.rasteronelab.lis.report.domain.model.LabReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for LabReport entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface LabReportMapper {

    @Mapping(target = "reportType", expression = "java(report.getReportType() != null ? report.getReportType().name() : null)")
    @Mapping(target = "reportStatus", expression = "java(report.getReportStatus() != null ? report.getReportStatus().name() : null)")
    ReportResponse toResponse(LabReport report);
}
