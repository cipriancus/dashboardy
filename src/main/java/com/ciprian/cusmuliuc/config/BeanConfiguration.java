package com.ciprian.cusmuliuc.config;

import com.ciprian.cusmuliuc.util.converter.MetricConverter;
import com.ciprian.cusmuliuc.util.converter.MetricTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
  @Bean
  public MetricTypeConverter metricTypeConverter() {
    return new MetricTypeConverter();
  }

  @Bean
  public MetricConverter metricConverter() {
    return new MetricConverter();
  }
}
