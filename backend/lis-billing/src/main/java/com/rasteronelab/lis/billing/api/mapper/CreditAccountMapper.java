package com.rasteronelab.lis.billing.api.mapper;

import com.rasteronelab.lis.billing.api.dto.CreditAccountRequest;
import com.rasteronelab.lis.billing.api.dto.CreditAccountResponse;
import com.rasteronelab.lis.billing.domain.model.CreditAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for CreditAccount entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface CreditAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "currentBalance", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    CreditAccount toEntity(CreditAccountRequest request);

    CreditAccountResponse toResponse(CreditAccount creditAccount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "currentBalance", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(CreditAccountRequest request, @MappingTarget CreditAccount creditAccount);
}
