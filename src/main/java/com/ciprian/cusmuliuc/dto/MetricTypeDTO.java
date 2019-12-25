package com.ciprian.cusmuliuc.dto;

import com.ciprian.cusmuliuc.model.InformationFormat;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * A MetricTypeDTO is a type of metric e.g: RAM, CPU The information format is tied to the value
 * type it can take Percentage, e.g: max 100% and MB e.g: >=0
 */
public class MetricTypeDTO {
  private long id;
  private String name;
  private InformationFormat informationFormat;
}
