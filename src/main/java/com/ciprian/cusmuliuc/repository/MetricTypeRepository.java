package com.ciprian.cusmuliuc.repository;

import com.ciprian.cusmuliuc.model.MetricType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MetricTypeRepository extends CrudRepository<MetricType, Long> {
  List<MetricType> getByName(String name);
}
