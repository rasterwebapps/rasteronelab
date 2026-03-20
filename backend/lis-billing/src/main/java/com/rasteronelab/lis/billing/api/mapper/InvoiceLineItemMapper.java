package com.rasteronelab.lis.billing.api.mapper;

import com.rasteronelab.lis.billing.api.dto.InvoiceLineItemRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceLineItemResponse;
import com.rasteronelab.lis.billing.domain.model.InvoiceLineItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for InvoiceLineItem entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface InvoiceLineItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    InvoiceLineItem toEntity(InvoiceLineItemRequest request);

    @Mapping(target = "invoiceId", source = "invoice.id")
    InvoiceLineItemResponse toResponse(InvoiceLineItem lineItem);
}
