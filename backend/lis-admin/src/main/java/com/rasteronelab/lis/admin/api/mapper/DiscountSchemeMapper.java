package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.DiscountSchemeRequest;
import com.rasteronelab.lis.admin.api.dto.DiscountSchemeResponse;
import com.rasteronelab.lis.admin.domain.model.DiscountScheme;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for DiscountScheme entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface DiscountSchemeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    DiscountScheme toEntity(DiscountSchemeRequest request);

    DiscountSchemeResponse toResponse(DiscountScheme discountScheme);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(DiscountSchemeRequest request, @MappingTarget DiscountScheme discountScheme);
}
