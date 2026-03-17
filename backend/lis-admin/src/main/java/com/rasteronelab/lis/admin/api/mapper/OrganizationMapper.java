package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.OrganizationRequest;
import com.rasteronelab.lis.admin.api.dto.OrganizationResponse;
import com.rasteronelab.lis.admin.domain.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for Organization entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    Organization toEntity(OrganizationRequest request);

    OrganizationResponse toResponse(Organization organization);

    void updateEntity(OrganizationRequest request, @MappingTarget Organization organization);
}
