package com.ciprian.cusmuliuc.util.converter;

import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.model.MetricType;

public class MetricTypeConverter extends AbstractConverter<MetricType, MetricTypeDTO> {

  @Override
  public MetricType convertToEntity(MetricTypeDTO dto) {
    return MetricType.builder()
        .name(dto.getName())
        .informationFormat(dto.getInformationFormat())
        .build();
  }

  @Override
  public MetricTypeDTO convertToDto(MetricType entity) {
    return MetricTypeDTO.builder()
        .id(entity.getId())
        .name(entity.getName())
        .informationFormat(entity.getInformationFormat())
        .build();
  }
}
