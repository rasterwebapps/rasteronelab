package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.NotificationTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.NotificationTemplateResponse;
import com.rasteronelab.lis.admin.domain.model.NotificationTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for NotificationTemplate entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface NotificationTemplateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    NotificationTemplate toEntity(NotificationTemplateRequest request);

    NotificationTemplateResponse toResponse(NotificationTemplate notificationTemplate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(NotificationTemplateRequest request, @MappingTarget NotificationTemplate notificationTemplate);
}
