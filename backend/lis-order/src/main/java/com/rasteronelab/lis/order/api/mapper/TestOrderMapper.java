package com.rasteronelab.lis.order.api.mapper;

import com.rasteronelab.lis.order.api.dto.TestOrderRequest;
import com.rasteronelab.lis.order.api.dto.TestOrderResponse;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for TestOrder entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring", uses = {OrderLineItemMapper.class})
public interface TestOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "barcode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "estimatedCompletionTime", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "lineItems", ignore = true)
    @Mapping(target = "priority", ignore = true)
    TestOrder toEntity(TestOrderRequest request);

    @Mapping(target = "priority", expression = "java(order.getPriority() != null ? order.getPriority().name() : null)")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().name() : null)")
    TestOrderResponse toResponse(TestOrder order);
}
