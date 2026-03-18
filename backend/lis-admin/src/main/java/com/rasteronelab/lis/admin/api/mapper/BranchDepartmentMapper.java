package com.rasteronelab.lis.admin.api.mapper;

import com.rasteronelab.lis.admin.api.dto.BranchDepartmentResponse;
import com.rasteronelab.lis.admin.domain.model.BranchDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for BranchDepartment entity → DTO conversion.
 */
@Mapper(componentModel = "spring")
public interface BranchDepartmentMapper {

    @Mapping(source = "branch.name", target = "branchName")
    @Mapping(source = "department.name", target = "departmentName")
    BranchDepartmentResponse toResponse(BranchDepartment branchDepartment);
}
