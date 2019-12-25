package com.ciprian.cusmuliuc.model;

/**
 * This object specifies inside MetricType the information format the values of Metric object have
 * if metric type is MB => Metric values must be >=0 if metric type is % => Metric values must be
 * >=0 and <=100
 */
public enum InformationFormat {
  MB("MB"),
  PERCENTAGE("%");

  private final String format;

  InformationFormat(String format) {
    this.format = format;
  }
}
