package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.TestMasterRequest;
import com.rasteronelab.lis.admin.api.dto.TestMasterResponse;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for TestMaster entity ↔ DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface TestMasterMapper {

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
    TestMaster toEntity(TestMasterRequest request);

    @Mapping(source = "department.name", target = "departmentName")
    TestMasterResponse toResponse(TestMaster testMaster);

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
    void updateEntity(TestMasterRequest request, @MappingTarget TestMaster testMaster);
}
