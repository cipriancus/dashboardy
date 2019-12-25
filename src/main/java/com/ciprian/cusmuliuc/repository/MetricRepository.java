package com.ciprian.cusmuliuc.repository;

import com.ciprian.cusmuliuc.model.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface MetricRepository extends CrudRepository<Metric, Long> {
  Optional<Metric> findByIdAndAndMetricTypeId(long id, long metricTypeId);

  Page<Metric> findAllByMetricType_IdAndDateBetween(Pageable page, long id, Date start, Date stop);
}
