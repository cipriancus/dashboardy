package com.ciprian.cusmuliuc.service;

import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeValuesDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.model.MetricType;
import com.ciprian.cusmuliuc.repository.MetricTypeRepository;
import com.ciprian.cusmuliuc.util.converter.MetricTypeConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
public class MetricTypeServiceTest {
  @InjectMocks MetricTypeService metricTypeService;

  @Mock MetricTypeRepository metricTypeRepository;

  @Mock MetricService metricService;

  @Spy MetricTypeConverter metricTypeConverter;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void whenGetAllMetricTypeIsCalled_thenShouldReturnListOfMetricTypeDtos() {
    Mockito.when(metricTypeRepository.findAll()).thenReturn(getMockMetricTypesList());

    List<MetricTypeDTO> dtos = metricTypeService.getAllMetricType();

    Assert.assertEquals(dtos.size(), 3);
    Assert.assertEquals(dtos.get(0).getName(), "CPU");
    Assert.assertEquals(dtos.get(1).getName(), "RAM");
    Assert.assertEquals(dtos.get(2).getName(), "GPU");

    Assert.assertEquals(dtos.get(0).getId(), 1);
    Assert.assertEquals(dtos.get(1).getId(), 2);
    Assert.assertEquals(dtos.get(2).getId(), 3);
  }

  @Test
  public void whenCreateMetricTypeIsCalledWithObject_thenShouldReturnDto() {
    Mockito.when(metricTypeRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetricType());
    Mockito.when(metricTypeRepository.getByName(ArgumentMatchers.anyString()))
        .thenReturn(Collections.emptyList());

    MetricTypeDTO metricTypeDTO = metricTypeService.createMetricType(getCpuMetricTypeDto());

    Assert.assertEquals(metricTypeDTO.getName(), "CPU");

    Assert.assertEquals(metricTypeDTO.getId(), 1);
  }

  @Test(expected = TechnicalException.class)
  public void whenCreateMetricTypeIsCalledWithObjectNameAlreadyExists_thenShouldReturnError() {
    Mockito.when(metricTypeRepository.getByName(ArgumentMatchers.anyString()))
        .thenReturn(getMockMetricTypesList());
    metricTypeService.createMetricType(getCpuMetricTypeDto());
  }

  @Test(expected = TechnicalException.class)
  public void whenUpdateMetricTypeIsCalledWithNonExistingIdAndObject_thenShouldThrowError() {
    Mockito.when(metricTypeRepository.save(ArgumentMatchers.any())).thenReturn(getCpuMetricType());
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    metricTypeService.updateMetricType("1", getCpuMetricTypeDto());
  }

  @Test
  public void whenUpdateMetricTypeIsCalledWithExistingIdAndObject_thenShouldReturnOk() {
    Mockito.when(metricTypeRepository.save(ArgumentMatchers.any())).thenReturn(getGPUMetricType());
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetricType()));

    MetricTypeDTO metricTypeDTO = metricTypeService.updateMetricType("1", getCpuMetricTypeDto());

    Assert.assertEquals(metricTypeDTO.getName(), "GPU");

    Assert.assertEquals(metricTypeDTO.getId(), 1);
  }

  @Test(expected = TechnicalException.class)
  public void whenDeleteMetricTypeIsCalledWithNonExistingId_thenShouldThrowError() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());
    metricTypeService.deleteMetricType("1");
  }

  @Test
  public void whenDeleteMetricTypeIsCalledWithExistingId_thenReturnOk() {
    MetricType cpu = getCpuMetricType();
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(cpu));

    metricTypeService.deleteMetricType("1");

    Mockito.verify(metricTypeRepository, Mockito.times(1)).delete(cpu);
  }

  @Test(expected = TechnicalException.class)
  public void whenGetMetricTypeByIdIsCalledWithNonExistingId_thenShouldThrowError() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());
    metricTypeService.getMetricTypeById("1");
  }

  @Test
  public void whenGetMetricTypeByIdIsCalledWithExistingId_thenReturnOk() {
    Mockito.when(metricTypeRepository.findById(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.of(getCpuMetricType()));

    MetricTypeDTO metricTypeDTO = metricTypeService.getMetricTypeById("1");

    Assert.assertEquals(metricTypeDTO.getName(), "CPU");

    Assert.assertEquals(metricTypeDTO.getId(), 1);
  }

  @Test
  public void whenGetAllMetricTypeMetricsIsCalled_thenReturnOk() {
    Mockito.when(metricTypeRepository.findAll()).thenReturn(getSingleMockMetricTypesList());

    Mockito.when(
            metricService.getAllMetricsForMetricType(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong()))
        .thenReturn(getPageMetric());

    List<MetricTypeValuesDTO> list =
        metricTypeService.getAllMetricTypeMetrics(0, 10, "DESC", 1L, 17L, 60L);

    Assert.assertEquals(list.size(), 1);
    Assert.assertEquals(list.get(0).getName(), "CPU");
    Assert.assertEquals(list.get(0).getMetrics().getSize(), 1);
  }

  private List<MetricType> getMockMetricTypesList() {
    List<MetricType> metricTypes = new ArrayList<>();
    metricTypes.add(MetricType.builder().id(1).name("CPU").build());
    metricTypes.add(MetricType.builder().id(2).name("RAM").build());
    metricTypes.add(MetricType.builder().id(3).name("GPU").build());
    return metricTypes;
  }

  private List<MetricType> getSingleMockMetricTypesList() {
    List<MetricType> metricTypes = new ArrayList<>();
    metricTypes.add(MetricType.builder().id(1).name("CPU").build());
    return metricTypes;
  }

  private MetricType getCpuMetricType() {
    return MetricType.builder().id(1).name("CPU").build();
  }

  private MetricType getGPUMetricType() {
    return MetricType.builder().id(1).name("GPU").build();
  }

  private MetricTypeDTO getCpuMetricTypeDto() {
    MetricTypeDTO metricTypeDTO = new MetricTypeDTO();
    metricTypeDTO.setName("CPU");
    return metricTypeDTO;
  }

  private Page<MetricDTO> getPageMetric() {
    MetricDTO metricDTO =
        MetricDTO.builder().id(1).value(100.00).metricType("CPU").date(new Date()).build();
    List<MetricDTO> list = new ArrayList<>();
    list.add(metricDTO);
    Page<MetricDTO> page = new PageImpl<>(list);
    return page;
  }
}
