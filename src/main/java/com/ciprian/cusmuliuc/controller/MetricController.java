package com.ciprian.cusmuliuc.controller;

import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles the metric object, a metric is an object that states what kind of
 * information is stored, e.g: CPU utilisation, GPU utilisation, RAM MB used etc
 */
@RestController
public class MetricController {

  private MetricService metricService;

  @Autowired
  public MetricController(MetricService metricService) {
    this.metricService = metricService;
  }

  /**
   * Creates a new meric
   *
   * @param metricTypeId
   * @param metricDTO the object of the new metric with value and date
   * @return the saved object, has the same info as dto but this one has the id
   */
  @PostMapping(path = "/metric-type/{metricTypeId}/metric/")
  @ResponseStatus(value = HttpStatus.OK)
  public MetricDTO createMetric(
      @PathVariable String metricTypeId, @RequestBody MetricDTO metricDTO) {
    return metricService.createMetric(metricTypeId, metricDTO);
  }

  /**
   * Update existing metric
   *
   * @param metricTypeId metric type that is associated with the metric
   * @param metricId metric id of the existing metric to be updated
   * @param metricDTO the object of the metric with value and date
   * @return the updated metric
   */
  @PutMapping(path = "/metric-type/{metricTypeId}/metric/{metricId}/")
  @ResponseStatus(value = HttpStatus.OK)
  public MetricDTO updateMetric(
      @PathVariable String metricTypeId,
      @PathVariable String metricId,
      @RequestBody MetricDTO metricDTO) {
    return metricService.updateMetric(metricTypeId, metricId, metricDTO);
  }

  /**
   * Delete a metric but metric id and metric type id
   *
   * @param metricTypeId metric type id associated with the metric
   * @param metricId the metric id to be deleted
   */
  @DeleteMapping(path = "/metric-type/{metricTypeId}/metric/{metricId}/")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteMetric(@PathVariable String metricTypeId, @PathVariable String metricId) {
    metricService.deleteMetric(metricTypeId, metricId);
  }

  /**
   * Return a paged response of all metric associated with a metric type
   *
   * <p>Can specify the page no, size of page and direction of sorting
   *
   * <p>Also it can sort by two start and end dated and have a time granularity of data
   *
   * @param metricTypeId the metric type id that identifies all metrics with
   * @param page page number, starts from 0
   * @param size size of a page, e.g: 100 elements in a page
   * @param direction of sorting of the time, it can be ASC or DESC
   * @param unixTimeFrom unix time starting from 1 which is 1970
   * @param unixTimeTo unix time that signals the end date
   * @param timeGranularity time granularity of the data, unix time, this specifies the distance
   *     between values
   * @return
   */
  @GetMapping(path = "/metric-type/{metricTypeId}/metric/")
  @ResponseStatus(value = HttpStatus.OK)
  public Page<MetricDTO> getAllMetricsForMetricType(
      @PathVariable String metricTypeId,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size,
      @RequestParam(required = false, defaultValue = "DESC") String direction,
      @RequestParam(required = false) Long unixTimeFrom,
      @RequestParam(required = false) Long unixTimeTo,
      @RequestParam(required = false) Long timeGranularity) {
    return metricService.getAllMetricsForMetricType(
        metricTypeId, page, size, direction, unixTimeFrom, unixTimeTo, timeGranularity);
  }
}
