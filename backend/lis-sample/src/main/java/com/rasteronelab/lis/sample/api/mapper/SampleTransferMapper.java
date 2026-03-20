package com.rasteronelab.lis.sample.api.mapper;

import com.rasteronelab.lis.sample.api.dto.SampleTransferResponse;
import com.rasteronelab.lis.sample.domain.model.SampleTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for SampleTransfer entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface SampleTransferMapper {

    @Mapping(target = "status", expression = "java(transfer.getStatus() != null ? transfer.getStatus().name() : null)")
    SampleTransferResponse toResponse(SampleTransfer transfer);
}
