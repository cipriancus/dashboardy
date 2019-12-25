package com.ciprian.cusmuliuc.service;

import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeValuesDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.model.InformationFormat;
import com.ciprian.cusmuliuc.model.Metric;
import com.ciprian.cusmuliuc.model.MetricType;
import com.ciprian.cusmuliuc.repository.MetricRepository;
import com.ciprian.cusmuliuc.repository.MetricTypeRepository;
import com.ciprian.cusmuliuc.util.converter.MetricConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class MetricServiceTest {
  @InjectMocks MetricService metricService;

  @Mock MetricTypeRepository metricTypeRepository;

  @Mock MetricRepository metricRepository;

  @Spy MetricConverter metricConverter;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = TechnicalException.class)
  public void whenCreateMetricIsCalledWithNonExistingId_thenShouldThrowException() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());
    metricService.createMetric("1", getMetricDto());
  }

  @Test(expected = TechnicalException.class)
  public void whenCreateMetricIsCalledWithIncorrectInformation_thenShouldThrowException() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetricType()));
    metricService.createMetric("1", getErrorMetricDto());
  }

  @Test
  public void whenCreateMetricIsCalledWithCorrectInformation_thenShouldReturnOk() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetricType()));

    Mockito.when(metricRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetric());

    MetricDTO metricDTO = metricService.createMetric("1", getMetricDto());

    Assert.assertEquals(metricDTO.getValue(), 100.0, 0);
    Assert.assertEquals(metricDTO.getMetricType(), "CPU");
  }

  @Test(expected = TechnicalException.class)
  public void whenUpdateMetricIsCalledWithNonExistingId_thenShouldThrowException() {
    Mockito.when(
            metricRepository.findByIdAndAndMetricTypeId(
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    metricService.updateMetric("1", "1", getMetricDto());
  }

  @Test
  public void whenUpdateMetricIsCalledWithIncorrectInformation_thenShouldNotModify() {
    Mockito.when(
            metricRepository.findByIdAndAndMetricTypeId(
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetric()));
    Mockito.when(metricRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetric());

    MetricDTO metricDTO = metricService.updateMetric("1", "1", getErrorMetricDto());
    Assert.assertEquals(metricDTO.getValue(), 100.0, 0);
  }

  @Test
  public void whenUpdateMetricIsCalledWithCorrectInformation_thenShouldModify() {
    Mockito.when(
            metricRepository.findByIdAndAndMetricTypeId(
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetric()));
    Mockito.when(metricRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetric());

    MetricDTO metricDTO = metricService.updateMetric("1", "1", getMetricDto());
    Assert.assertEquals(metricDTO.getValue(), 100.0, 0);
  }

  @Test
  public void whenDeleteMetricIsCalledWithCorrectInformation_thenShouldModify() {
    Mockito.when(
            metricRepository.findByIdAndAndMetricTypeId(
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetric()));
    Mockito.when(metricRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetric());

    metricService.deleteMetric("1", "1");
  }

  @Test
  public void whenGetAllMetricsForMetricTypeIsCalledWithCorrectInformation_thenShouldReturnPage() {
    Mockito.when(
            metricRepository.findAllByMetricType_IdAndDateBetween(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()))
        .thenReturn((Page) new PageImpl<>(getMetricDtoList()));

    Page<MetricDTO> page =
        metricService.getAllMetricsForMetricType("1", 0, 10, "DESC", 1L, 17L, null);
    Assert.assertEquals(page.getSize(), 3);
  }

  @Test
  public void whenGetAllMetricsForMetricTypeIsCalledWithNoStartDate_thenShouldReturnPage() {
    Mockito.when(
            metricRepository.findAllByMetricType_IdAndDateBetween(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                Mockito.eq(new Date(1L)),
                ArgumentMatchers.any()))
        .thenReturn((Page) new PageImpl<>(getMetricDtoList()));

    Page<MetricDTO> page =
        metricService.getAllMetricsForMetricType("1", 0, 10, "DESC", null, 17L, null);
    Assert.assertEquals(page.getSize(), 3);
  }

  @Test
  public void
      whenGetAllMetricsForMetricTypeIsCalledWithNoStartDateNoEndDate_thenShouldReturnPage() {
    Mockito.when(
            metricRepository.findAllByMetricType_IdAndDateBetween(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                Mockito.eq(new Date(1L)),
                ArgumentMatchers.any()))
        .thenReturn((Page) new PageImpl<>(getMetricDtoList()));

    Page<MetricDTO> page =
        metricService.getAllMetricsForMetricType("1", 0, 10, "DESC", null, null, null);
    Assert.assertEquals(page.getSize(), 3);
  }

  @Test
  public void whenGetAllMetricsForMetricTypeIsCalledWithNoEndDate_thenShouldReturnPage() {
    Mockito.when(
            metricRepository.findAllByMetricType_IdAndDateBetween(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any()))
        .thenReturn((Page) new PageImpl<>(getMetricDtoList()));

    Page<MetricDTO> page =
        metricService.getAllMetricsForMetricType("1", 0, 10, "DESC", 1700L, null, null);
    Assert.assertEquals(page.getSize(), 3);
  }

  private MetricDTO getMetricDto() {
    return MetricDTO.builder().id(1).value(100.00).metricType("CPU").date(new Date()).build();
  }

  private List<Metric> getMetricDtoList() {
    List<Metric> list = new ArrayList<>();
    list.add(
        Metric.builder()
            .id(1)
            .value(100.00)
            .metricType(getCpuMetricType())
            .date(new Date())
            .build());
    list.add(
        Metric.builder().id(2).value(98.0).metricType(getCpuMetricType()).date(new Date()).build());
    list.add(
        Metric.builder().id(3).value(95.0).metricType(getCpuMetricType()).date(new Date()).build());
    return list;
  }

  private MetricDTO getErrorMetricDto() {
    return MetricDTO.builder().id(1).value(105.00).metricType("CPU").date(new Date()).build();
  }

  private Metric getCpuMetric() {
    return Metric.builder()
        .id(1)
        .value(100.0)
        .date(new Date())
        .metricType(getCpuMetricType())
        .build();
  }

  private MetricType getCpuMetricType() {
    return MetricType.builder()
        .id(1)
        .name("CPU")
        .informationFormat(InformationFormat.PERCENTAGE)
        .build();
  }
}
