package com.rasteronelab.lis.sample.api.mapper;

import com.rasteronelab.lis.sample.api.dto.SampleTrackingResponse;
import com.rasteronelab.lis.sample.domain.model.SampleTracking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for SampleTracking entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface SampleTrackingMapper {

    @Mapping(target = "status", expression = "java(tracking.getStatus() != null ? tracking.getStatus().name() : null)")
    SampleTrackingResponse toResponse(SampleTracking tracking);
}
