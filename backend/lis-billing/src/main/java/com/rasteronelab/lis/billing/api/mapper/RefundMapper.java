package com.rasteronelab.lis.billing.api.mapper;

import com.rasteronelab.lis.billing.api.dto.RefundResponse;
import com.rasteronelab.lis.billing.domain.model.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Refund entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface RefundMapper {

    @Mapping(target = "invoiceId", source = "invoice.id")
    RefundResponse toResponse(Refund refund);
}
