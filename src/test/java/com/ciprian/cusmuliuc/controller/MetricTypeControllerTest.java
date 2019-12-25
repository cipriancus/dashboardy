package com.ciprian.cusmuliuc.controller;

import com.ciprian.cusmuliuc.config.ResponseExceptionHandler;
import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeDTO;
import com.ciprian.cusmuliuc.dto.MetricTypeValuesDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import com.ciprian.cusmuliuc.model.InformationFormat;
import com.ciprian.cusmuliuc.service.MetricTypeService;
import com.ciprian.cusmuliuc.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MetricTypeControllerTest {
  @Autowired private MockMvc mockMvc;

  @InjectMocks MetricTypeController metricTypeController;

  @MockBean private MetricTypeService metricTypeService;

  private static final String ID = "1";

  private static final String ERROR_ID = "55";

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(metricTypeController)
            .setControllerAdvice(new ResponseExceptionHandler())
            .build();
  }

  @Test
  public void whenGetAllMetricTypeIsCalled_ShouldReturnListOfMetricTypes() throws Exception {
    Mockito.when(metricTypeService.getAllMetricType()).thenReturn(getMockMetricTypesList());

    this.mockMvc
        .perform(get("/metric-type/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("RAM")));
  }

  @Test
  public void whenGetMetricTypeByIdIsCalledWithCorrectId_ShouldReturnMetricType() throws Exception {
    Mockito.when(metricTypeService.getMetricTypeById(ID)).thenReturn(getCpuMetricType());

    this.mockMvc
        .perform(get("/metric-type/" + ID + "/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("CPU")));
  }

  @Test
  public void whenGetMetricTypeByIdIsCalledWithErrorId_ShouldReturnException() throws Exception {
    Mockito.when(metricTypeService.getMetricTypeById(ERROR_ID))
        .thenThrow(new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST));

    this.mockMvc
        .perform(get("/metric-type/" + ERROR_ID + "/"))
        .andDo(print())
        .andExpect(status().is5xxServerError());
  }

  @Test
  public void whenCreateMetricTypeIsCalled_ShouldReturnMetricType() throws Exception {
    Mockito.when(metricTypeService.createMetricType(ArgumentMatchers.any()))
        .thenReturn(getCpuMetricType());

    this.mockMvc
        .perform(
            post("/metric-type/")
                .content("{\"name\":\"CPU\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("CPU")));
  }

  @Test
  public void whenCreateMetricTypeIsCalled_ShouldReturnError() throws Exception {
    Mockito.when(metricTypeService.createMetricType(ArgumentMatchers.any()))
        .thenThrow(new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST));

    this.mockMvc
        .perform(
            post("/metric-type/")
                .content("{\"name\":\"CPU\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is5xxServerError());
  }

  @Test
  public void whenCreateMetricTypeIsCalledWithNull_ShouldReturnBadRequest() throws Exception {
    this.mockMvc.perform(post("/metric-type/")).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void whenUpdateMetricTypeIsCalled_ShouldReturnMetricType() throws Exception {
    Mockito.when(
            metricTypeService.updateMetricType(
                ArgumentMatchers.anyString(), ArgumentMatchers.any()))
        .thenReturn(getCpuMetricType());

    this.mockMvc
        .perform(
            put("/metric-type/" + ID + "/")
                .content("{\"name\":\"CPU\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("CPU")));
  }

  @Test
  public void whenUpdateMetricTypeIsCalled_ShouldReturnError() throws Exception {
    Mockito.when(
            metricTypeService.updateMetricType(
                ArgumentMatchers.anyString(), ArgumentMatchers.any()))
        .thenThrow(new TechnicalException(Constants.METRIC_TYPE_ID_DOES_NOT_EXIST));

    this.mockMvc
        .perform(
            put("/metric-type/" + ID + "/")
                .content("{\"name\":\"CPU\"}")
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is5xxServerError());
  }

  @Test
  public void whenDeleteMetricTypeIsCalled_ShouldReturnOk() throws Exception {

    this.mockMvc
        .perform(delete("/metric-type/" + ID + "/"))
        .andDo(print())
        .andExpect(status().isOk());

    Mockito.verify(metricTypeService, Mockito.times(1)).deleteMetricType(ID);
  }

  @Test
  public void whenGetAllMetricsForMetricTypeIsCalled_ShouldReturnMetricType() throws Exception {
    Mockito.when(
            metricTypeService.getAllMetricTypeMetrics(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong()))
        .thenReturn(getPageMetrics());

    this.mockMvc
        .perform(
            get("/metric-type/metric/")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "DESC")
                .param("unixTimeFrom", "1")
                .param("unixTimeTo", "1000")
                .param("timeGranularity", "60"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("100.0")));
  }

  private List<MetricTypeDTO> getMockMetricTypesList() {
    List<MetricTypeDTO> metricTypeDtos = new ArrayList<>();
    metricTypeDtos.add(
        MetricTypeDTO.builder()
            .id(1)
            .name("CPU")
            .informationFormat(InformationFormat.PERCENTAGE)
            .build());
    metricTypeDtos.add(
        MetricTypeDTO.builder().id(2).name("RAM").informationFormat(InformationFormat.MB).build());
    metricTypeDtos.add(
        MetricTypeDTO.builder()
            .id(3)
            .name("GPU")
            .informationFormat(InformationFormat.PERCENTAGE)
            .build());
    return metricTypeDtos;
  }

  private MetricTypeDTO getCpuMetricType() {
    return MetricTypeDTO.builder()
        .id(1)
        .name("CPU")
        .informationFormat(InformationFormat.PERCENTAGE)
        .build();
  }

  private List<MetricTypeValuesDTO> getPageMetrics() {
    MetricTypeValuesDTO metricDTO =
        MetricTypeValuesDTO.builder()
            .id(1)
            .informationFormat(InformationFormat.MB)
            .name("CPU")
            .metrics(getPageMetric())
            .build();

    List<MetricTypeValuesDTO> list = new ArrayList<>();
    list.add(metricDTO);

    return list;
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
