package com.ciprian.cusmuliuc.controller;

import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeValuesDTO;
import com.ciprian.cusmuliuc.service.MetricTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller that handles the metric type object, a metric is an object that states what kind of
 * information is stored, e.g: CPU, GPU, RAM etc
 */
@RestController
@RequestMapping(path = "/metric-type")
public class MetricTypeController {
  private MetricTypeService metricTypeService;

  @Autowired
  public MetricTypeController(MetricTypeService metricTypeService) {
    this.metricTypeService = metricTypeService;
  }

  /**
   * Get all the metric types in the system
   *
   * @return a list of metrics dtos
   */
  @GetMapping(path = "/")
  @ResponseStatus(value = HttpStatus.OK)
  public List<MetricTypeDTO> getAllMetricType() {
    return metricTypeService.getAllMetricType();
  }

  /**
   * Endpoint for returning all metric type information but also has pages of data inside them
   *
   * @param page page number, starts from 0
   * @param size size of a page, e.g: 100 elements in a page
   * @param direction of sorting of the time, it can be ASC or DESC
   * @param unixTimeFrom unix time starting from 1 which is 1970
   * @param unixTimeTo unix time that signals the end date
   * @param timeGranularity time granularity of the data, unix time, this specifies the distance
   *     between values
   * @return a list of all metrics and pages with values
   */
  @GetMapping(path = "/metric/")
  @ResponseStatus(value = HttpStatus.OK)
  public List<MetricTypeValuesDTO> getAllMetricTypeMetrics(
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      @RequestParam(required = false, defaultValue = "DESC") String direction,
      @RequestParam(required = false) Long unixTimeFrom,
      @RequestParam(required = false) Long unixTimeTo,
      @RequestParam(required = false) Long timeGranularity) {
    return metricTypeService.getAllMetricTypeMetrics(
        page, size, direction, unixTimeFrom, unixTimeTo, timeGranularity);
  }

  /**
   * Create a new metric type in the database
   *
   * @param metricType is the object to be created
   * @return a metric type dto that has been created
   */
  @PostMapping(path = "/")
  @ResponseStatus(value = HttpStatus.OK)
  public MetricTypeDTO createMetricType(@RequestBody MetricTypeDTO metricType) {
    return metricTypeService.createMetricType(metricType);
  }

  /**
   * Get a metric by the id, e.g: 1 should be CPU
   *
   * @param id the system id of the metric
   * @return a metric object from the database
   */
  @GetMapping(path = "/{id}/")
  @ResponseStatus(value = HttpStatus.OK)
  public MetricTypeDTO getMetricTypeById(@PathVariable String id) {
    return metricTypeService.getMetricTypeById(id);
  }

  /**
   * Update a metric using the id and new information
   *
   * @param id of the existing metric
   * @param metricType the new info that is used for update
   * @return the new updated metric
   */
  @PutMapping(path = "/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public MetricTypeDTO updateMetricType(
      @PathVariable String id, @RequestBody MetricTypeDTO metricType) {
    return metricTypeService.updateMetricType(id, metricType);
  }

  /**
   * Delete a metric by id
   *
   * @param id the id of the existing metric
   */
  @DeleteMapping(path = "/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteMetricType(@PathVariable String id) {
    metricTypeService.deleteMetricType(id);
  }
}
