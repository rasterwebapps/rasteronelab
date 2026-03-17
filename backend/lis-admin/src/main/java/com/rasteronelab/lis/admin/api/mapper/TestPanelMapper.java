package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.TestPanelRequest;
import com.rasteronelab.lis.admin.api.dto.TestPanelResponse;
import com.rasteronelab.lis.admin.domain.model.TestPanel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for TestPanel entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface TestPanelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    TestPanel toEntity(TestPanelRequest request);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(target = "tests", ignore = true)
    TestPanelResponse toResponse(TestPanel testPanel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branchId", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(TestPanelRequest request, @MappingTarget TestPanel testPanel);
}
