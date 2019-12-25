package com.ciprian.cusmuliuc.controller;

import com.ciprian.cusmuliuc.config.ResponseExceptionHandler;
import com.ciprian.cusmuliuc.dto.MetricDTO;
import com.ciprian.cusmuliuc.service.MetricService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MetricControllerTest {
  @Autowired private MockMvc mockMvc;

  @InjectMocks private MetricController metricController;

  @MockBean private MetricService metricService;

  private static final String ID = "1";

  private static final String ERROR_ID = "55";

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(metricController)
            .setControllerAdvice(new ResponseExceptionHandler())
            .build();
  }

  @Test
  public void whenCreateMetricIsCalled_ShouldReturnMetricType() throws Exception {
    Mockito.when(metricService.createMetric(ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(getCpuMetric());

    this.mockMvc
        .perform(
            post("/metric-type/1/metric/")
                .content("{\"value\":100.0,\"date\":\"2019-12-23T12:13:54.847+0000\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("100.0")));
  }

  @Test
  public void whenUpdateMetricIsCalled_ShouldReturnMetricType() throws Exception {
    Mockito.when(
            metricService.updateMetric(
                ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
        .thenReturn(getCpuMetric());

    this.mockMvc
        .perform(
            put("/metric-type/1/metric/1/")
                .content("{\"value\":100.0,\"date\":\"2019-12-23T12:13:54.847+0000\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("100.0")));
  }

  @Test
  public void whenDeleteMetricIsCalled_ShouldReturnMetricType() throws Exception {

    this.mockMvc
        .perform(delete("/metric-type/1/metric/1/"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void whenGetAllMetricsForMetricTypeIsCalled_ShouldReturnMetricType() throws Exception {
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

    this.mockMvc
        .perform(
            get("/metric-type/1/metric/")
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

  private MetricDTO getCpuMetric() {
    return MetricDTO.builder().metricType("CPU").date(new Date()).id(1).value(100.0).build();
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
