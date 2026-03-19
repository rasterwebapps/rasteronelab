package com.rasteronelab.lis.billing.api.mapper;

import com.rasteronelab.lis.billing.api.dto.PaymentResponse;
import com.rasteronelab.lis.billing.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Payment entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "invoiceNumber", source = "invoice.invoiceNumber")
    PaymentResponse toResponse(Payment payment);
}
