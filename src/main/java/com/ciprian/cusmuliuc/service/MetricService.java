package com.ciprian.cusmuliuc.service;

import com.ciprian.cusmuliuc.aspect.log.LogMethod;
import com.ciprian.cusmuliuc.aspect.verify.VerifyArguments;
import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.model.InformationFormat;
import com.ciprian.cusmuliuc.model.Metric;
import com.ciprian.cusmuliuc.model.MetricType;
import com.ciprian.cusmuliuc.repository.MetricRepository;
import com.ciprian.cusmuliuc.repository.MetricTypeRepository;
import com.ciprian.cusmuliuc.util.Constants;
import com.ciprian.cusmuliuc.util.converter.MetricConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class MetricService {

  private MetricRepository metricRepository;
  private MetricTypeRepository metricTypeRepository;
  private MetricConverter metricConverter;

  @Autowired
  public MetricService(
      MetricRepository metricRepository,
      MetricTypeRepository metricTypeRepository,
      MetricConverter metricConverter) {
    this.metricRepository = metricRepository;
    this.metricTypeRepository = metricTypeRepository;
    this.metricConverter = metricConverter;
  }

  @LogMethod
  @VerifyArguments
  public MetricDTO createMetric(String metricTypeId, MetricDTO metricDTO) {
    MetricType metricType = verifyMetricTypeExistence(metricTypeId);

    if (!isCorrectMetricValue(metricType, metricDTO)) {
      throw new TechnicalException(Constants.METRIC_VALUE_IS_NOT_CORRECT);
    }

    Metric metric = metricConverter.convertToEntity(metricDTO);
    metric.setMetricType(metricType);

    metric = metricRepository.save(metric);
    return metricConverter.convertToDto(metric);
  }

  @LogMethod
  @VerifyArguments
  public MetricDTO updateMetric(String metricTypeId, String metricId, MetricDTO metricDTO) {
    Metric metric = getMetricByIdAndMetricTypeId(metricTypeId, metricId);

    if (metricDTO.getValue() != null && isCorrectMetricValue(metric.getMetricType(), metricDTO)) {
      metric.setValue(metricDTO.getValue());
    }

    if (metricDTO.getDate() != null) {
      metric.setDate(metricDTO.getDate());
    }

    metricRepository.save(metric);

    return metricConverter.convertToDto(metric);
  }

  @LogMethod
  @VerifyArguments
  public void deleteMetric(String metricTypeId, String metricId) {
    metricRepository.delete(getMetricByIdAndMetricTypeId(metricTypeId, metricId));
  }

  @LogMethod
  public Page<MetricDTO> getAllMetricsForMetricType(
      String metricTypeId,
      int page,
      int size,
      String direction,
      Long unixTimeFrom,
      Long unixTimeTo,
      Long timeGranularity) {

    Date start;
    Date stop;

    if (unixTimeFrom == null) {
      unixTimeFrom = 1L;
      start = new Date(unixTimeFrom);
    } else {
      start = new Date(unixTimeFrom * 1000L);
    }

    if (unixTimeTo == null) {
      unixTimeTo = System.currentTimeMillis();
      stop = new Date(unixTimeTo);
    } else {
      stop = new Date(unixTimeTo * 1000L);
    }

    Page<Metric> entities =
        metricRepository.findAllByMetricType_IdAndDateBetween(
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), "date")),
            Long.parseLong(metricTypeId),
            start,
            stop);

    Page<MetricDTO> dtoPage = entities.map(entity -> metricConverter.convertToDto(entity));

    if (timeGranularity != null && timeGranularity > 0) {
      timeGranularity = timeGranularity * 1000L;
      dtoPage = calculateTimeGranularity(dtoPage, timeGranularity);
    }

    return dtoPage;
  }

  private Metric getMetricByIdAndMetricTypeId(String metricTypeId, String metricId) {
    Optional<Metric> metric =
        metricRepository.findByIdAndAndMetricTypeId(
            Long.parseLong(metricTypeId), Long.parseLong(metricId));

    if (!metric.isPresent()) {
      throw new TechnicalException(Constants.METRIC_ID_DOES_NOT_EXIST);
    }
    return metric.get();
  }

  private boolean isCorrectMetricValue(MetricType metricType, MetricDTO metricDTO) {
    return (metricType.getInformationFormat() == InformationFormat.PERCENTAGE
            && metricDTO.getValue() >= 0
            && metricDTO.getValue() <= 100
        || metricType.getInformationFormat() == InformationFormat.MB && metricDTO.getValue() >= 0);
  }

  private MetricType verifyMetricTypeExistence(String id) {
    Optional<MetricType> metricTypeOptional = metricTypeRepository.findById(Long.parseLong(id));

    if (!metricTypeOptional.isPresent()) {
      throw new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST);
    }

    return metricTypeOptional.get();
  }

  private Page<MetricDTO> calculateTimeGranularity(Page<MetricDTO> dtoPage, Long timeGranularity) {

    List<MetricDTO> dtos = dtoPage.getContent();

    List<MetricDTO> compressedList = new LinkedList<>();

    String metricType = dtos.get(0).getMetricType();

    int iterator = 0;
    while (iterator < dtos.size() - 1) {
      double sum = dtos.get(iterator).getValue();
      long time = dtos.get(iterator).getDate().getTime();
      long elements = 1;
      int iterator2 = iterator + 1;
      while (iterator2 < dtos.size()) {
        double difference =
            Math.abs(
                dtos.get(iterator2).getDate().getTime() - dtos.get(iterator).getDate().getTime());
        if (difference > timeGranularity || difference == timeGranularity) {
          if (elements > 1) {
            sum = sum / elements;
            time = time / elements;
            MetricDTO metricDTO = new MetricDTO();
            metricDTO.setDate(new Date(time));
            metricDTO.setValue(sum);
            metricDTO.setMetricType(metricType);
            compressedList.add(metricDTO);
          } else {
            compressedList.add(dtos.get(iterator));
            if (iterator2 == dtos.size() - 1) {
              compressedList.add(dtos.get(iterator2));
            }
          }
          iterator = iterator2;
          break;
        } else if (difference < timeGranularity) {
          sum = sum + dtos.get(iterator2).getValue();
          time = time + dtos.get(iterator2).getDate().getTime();
          elements++;
        }
        iterator2++;
      }
      if (iterator2 == dtos.size() && elements > 1) {
        sum = sum / elements;
        time = time / elements;
        MetricDTO metricDTO = new MetricDTO();
        metricDTO.setDate(new Date(time));
        metricDTO.setValue(sum);
        compressedList.add(metricDTO);
        iterator = iterator2;
      }
    }
    return new PageImpl<>(compressedList);
  }
}
