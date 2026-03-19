package com.rasteronelab.lis.billing.api.mapper;

import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Invoice entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring", uses = {InvoiceLineItemMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "paidAmount", ignore = true)
    @Mapping(target = "balanceAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "invoiceDate", ignore = true)
    @Mapping(target = "dueDate", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "lineItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Invoice toEntity(InvoiceRequest request);

    InvoiceResponse toResponse(Invoice invoice);
}
