package com.ciprian.cusmuliuc.service;

import com.ciprian.cusmuliuc.aspect.log.LogMethod;
import com.ciprian.cusmuliuc.aspect.verify.VerifyArguments;
import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeValuesDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.model.MetricType;
import com.ciprian.cusmuliuc.repository.MetricTypeRepository;
import com.ciprian.cusmuliuc.util.Constants;
import com.ciprian.cusmuliuc.util.converter.MetricTypeConverter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MetricTypeService {
  private MetricTypeRepository metricTypeRepository;
  private MetricTypeConverter metricTypeConverter;
  private MetricService metricService;

  @Autowired
  public MetricTypeService(
      MetricTypeRepository metricTypeRepository,
      MetricTypeConverter metricTypeConverter,
      MetricService metricService) {
    this.metricTypeRepository = metricTypeRepository;
    this.metricTypeConverter = metricTypeConverter;
    this.metricService = metricService;
  }

  @LogMethod
  public List<MetricTypeDTO> getAllMetricType() {
    return metricTypeConverter.convertToDtoList(Lists.newArrayList(metricTypeRepository.findAll()));
  }

  @LogMethod
  @VerifyArguments
  public MetricTypeDTO createMetricType(MetricTypeDTO metricType) {
    verifyMetricTypeExistenceByName(metricType.getName());

    MetricType newMetricType = metricTypeConverter.convertToEntity(metricType);

    newMetricType = metricTypeRepository.save(newMetricType);

    return metricTypeConverter.convertToDto(newMetricType);
  }

  @VerifyArguments
  public MetricTypeDTO updateMetricType(String id, MetricTypeDTO metricTypeDTO) {
    MetricType metricType = verifyMetricTypeExistence(id);

    metricType.setName(metricTypeDTO.getName());
    metricType.setInformationFormat(metricTypeDTO.getInformationFormat());

    metricType = metricTypeRepository.save(metricType);

    return metricTypeConverter.convertToDto(metricType);
  }

  @VerifyArguments
  public void deleteMetricType(String id) {
    metricTypeRepository.delete(verifyMetricTypeExistence(id));
  }

  @VerifyArguments
  public MetricTypeDTO getMetricTypeById(String id) {
    return metricTypeConverter.convertToDto(getMetricTypeEntityById(id));
  }

  @VerifyArguments
  public MetricType getMetricTypeEntityById(String id) {
    return verifyMetricTypeExistence(id);
  }

  @LogMethod
  public List<MetricTypeValuesDTO> getAllMetricTypeMetrics(
      int page,
      int size,
      String direction,
      Long unixTimeFrom,
      Long unixTimeTo,
      Long timeGranularity) {
    List<MetricTypeValuesDTO> metricTypeValuesDTOS = new ArrayList<>();

    List<MetricTypeDTO> allMetricTypes = getAllMetricType();

    for (MetricTypeDTO iterator : allMetricTypes) {
      Page<MetricDTO> pageMetric =
          metricService.getAllMetricsForMetricType(
              Long.toString(iterator.getId()),
              page,
              size,
              direction,
              unixTimeFrom,
              unixTimeTo,
              timeGranularity);
      metricTypeValuesDTOS.add(new MetricTypeValuesDTO(iterator, pageMetric));
    }
    return metricTypeValuesDTOS;
  }

  private MetricType verifyMetricTypeExistence(String id) {
    Optional<MetricType> metricTypeOptional = metricTypeRepository.findById(Long.parseLong(id));

    if (!metricTypeOptional.isPresent()) {
      throw new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST);
    }

    return metricTypeOptional.get();
  }

  private void verifyMetricTypeExistenceByName(String name) {
    List<MetricType> existingMetricTypes = metricTypeRepository.getByName(name);

    if (existingMetricTypes.size() > 0) {
      throw new TechnicalException(Constants.METRIC_TYPE_NAME_ALREADY_EXISTS);
    }
  }
}
