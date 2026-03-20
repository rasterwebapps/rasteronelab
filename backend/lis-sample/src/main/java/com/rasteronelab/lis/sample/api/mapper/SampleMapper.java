package com.rasteronelab.lis.sample.api.mapper;

import com.rasteronelab.lis.sample.api.dto.SampleResponse;
import com.rasteronelab.lis.sample.domain.model.Sample;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Sample entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface SampleMapper {

    @Mapping(target = "status", expression = "java(sample.getStatus() != null ? sample.getStatus().name() : null)")
    @Mapping(target = "tubeType", expression = "java(sample.getTubeType() != null ? sample.getTubeType().name() : null)")
    @Mapping(target = "collectionSite", expression = "java(sample.getCollectionSite() != null ? sample.getCollectionSite().name() : null)")
    @Mapping(target = "rejectionReason", expression = "java(sample.getRejectionReason() != null ? sample.getRejectionReason().name() : null)")
    SampleResponse toResponse(Sample sample);
}
