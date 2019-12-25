package com.ciprian.cusmuliuc.util.converter;

import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.model.Metric;

public class MetricConverter extends AbstractConverter<Metric, MetricDTO> {
  @Override
  public Metric convertToEntity(MetricDTO dto) {
    return Metric.builder().date(dto.getDate()).value(dto.getValue()).id(dto.getId()).build();
  }

  @Override
  public MetricDTO convertToDto(Metric entity) {
    return MetricDTO.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .metricType(entity.getMetricType().getName())
        .value(entity.getValue())
        .build();
  }
}
