package com.ciprian.cusmuliuc.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * A MetricDTO is simply an object that explains a Metric, it has a value, time and a metric type
 * e.g: 100% CPU at a certain time
 */
public class MetricDTO {
  private long id;

  private Double value;

  private String metricType; // this is basically the tag

  private Date date;
}
