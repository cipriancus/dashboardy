package com.ciprian.cusmuliuc.dto;

import com.ciprian.cusmuliuc.model.InformationFormat;
import lombok.*;
import org.springframework.data.domain.Page;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/** Same as MetricTypeDTO but this one has the metrics Page that actually return information */
public class MetricTypeValuesDTO {
  private long id;
  private String name;
  private InformationFormat informationFormat;
  Page<MetricDTO> metrics;

  public MetricTypeValuesDTO(MetricTypeDTO metricType, Page<MetricDTO> metrics) {
    this(metricType.getId(), metricType.getName(), metricType.getInformationFormat(), metrics);
  }
}
