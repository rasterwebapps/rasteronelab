package com.rasteronelab.lis.order.api.mapper;

import com.rasteronelab.lis.order.api.dto.OrderLineItemRequest;
import com.rasteronelab.lis.order.api.dto.OrderLineItemResponse;
import com.rasteronelab.lis.order.domain.model.OrderLineItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for OrderLineItem entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface OrderLineItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "netPrice", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    OrderLineItem toEntity(OrderLineItemRequest request);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(target = "status", expression = "java(lineItem.getStatus() != null ? lineItem.getStatus().name() : null)")
    OrderLineItemResponse toResponse(OrderLineItem lineItem);
}
