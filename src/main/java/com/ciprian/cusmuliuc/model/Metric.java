package com.ciprian.cusmuliuc.model;

import lombok.*;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Date;

@Entity(name = "Metric")
@Table(name = "metric")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column @Nonnull
  private Double value; // value cannot be null, this is the value of the metric, e.g:100% CPU

  @ManyToOne private MetricType metricType; // this is basically the tag

  @Column @Nonnull private Date date;
}
