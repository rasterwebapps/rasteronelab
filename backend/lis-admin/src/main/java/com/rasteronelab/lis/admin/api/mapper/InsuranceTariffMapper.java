package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.InsuranceTariffRequest;
import com.rasteronelab.lis.admin.api.dto.InsuranceTariffResponse;
import com.rasteronelab.lis.admin.domain.model.InsuranceTariff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for InsuranceTariff entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface InsuranceTariffMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    InsuranceTariff toEntity(InsuranceTariffRequest request);

    InsuranceTariffResponse toResponse(InsuranceTariff insuranceTariff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(InsuranceTariffRequest request, @MappingTarget InsuranceTariff insuranceTariff);
}
