package com.ciprian.cusmuliuc.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "MetricType")
@Table(name = "metric_type")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricType {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  private InformationFormat informationFormat;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "metricType", orphanRemoval = true)
  private List<Metric> metrics;
}
