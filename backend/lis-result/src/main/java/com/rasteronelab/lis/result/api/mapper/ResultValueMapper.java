package com.rasteronelab.lis.result.api.mapper;

import com.rasteronelab.lis.result.api.dto.ResultValueResponse;
import com.rasteronelab.lis.result.domain.model.ResultValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResultValueMapper {

    @Mapping(target = "resultType", expression = "java(resultValue.getResultType() != null ? resultValue.getResultType().name() : null)")
    @Mapping(target = "abnormalFlag", expression = "java(resultValue.getAbnormalFlag() != null ? resultValue.getAbnormalFlag().name() : null)")
    @Mapping(target = "deltaCheckStatus", expression = "java(resultValue.getDeltaCheckStatus() != null ? resultValue.getDeltaCheckStatus().name() : null)")
    ResultValueResponse toResponse(ResultValue resultValue);
}
