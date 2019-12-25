package com.ciprian.cusmuliuc.config;

import com.ciprian.cusmuliuc.model.InformationFormat;
import com.ciprian.cusmuliuc.model.Metric;
import com.ciprian.cusmuliuc.model.MetricType;
import com.ciprian.cusmuliuc.repository.MetricRepository;
import com.ciprian.cusmuliuc.repository.MetricTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Random;

@Configuration
public class InitDatabaseConfiguration {
  private static final long TIMESTAMP = System.currentTimeMillis();

  @Bean
  public CommandLineRunner initDatabase(
      MetricTypeRepository metricTypeRepository, MetricRepository metricRepository) {
    return args -> {
      MetricType cpu =
          MetricType.builder().name("CPU").informationFormat(InformationFormat.PERCENTAGE).build();
      MetricType ram =
          MetricType.builder().name("RAM").informationFormat(InformationFormat.MB).build();
      MetricType gpu =
          MetricType.builder().name("GPU").informationFormat(InformationFormat.PERCENTAGE).build();
      MetricType disk =
          MetricType.builder().name("DISK").informationFormat(InformationFormat.MB).build();
      MetricType network =
          MetricType.builder().name("NETWORK").informationFormat(InformationFormat.MB).build();

      /** I created some example metric types */
      cpu = metricTypeRepository.save(cpu);
      ram = metricTypeRepository.save(ram);
      gpu = metricTypeRepository.save(gpu);
      disk = metricTypeRepository.save(disk);
      network = metricTypeRepository.save(network);

      /** for every metric type create some metric values randomly */
      populateDatabase(metricRepository, cpu);
      populateDatabase(metricRepository, ram);
      populateDatabase(metricRepository, gpu);
      populateDatabase(metricRepository, disk);
      populateDatabase(metricRepository, network);
    };
  }

  private void populateDatabase(MetricRepository metricRepository, MetricType metricType) {
    for (int iterator = 0;
        iterator < 100_000;
        iterator++) { // i want to create 100_000 enties for every metric
      Metric metric = null;
      long seconds =
          TIMESTAMP
              - (iterator * 60
                  * 1000L); // add every second a metric we must multiply with 1000 to convert to
      // milliseconds
      if (metricType.getInformationFormat() == InformationFormat.PERCENTAGE) {
        metric =
            Metric.builder()
                .value((double) new Random().nextInt(100)) // just a random value from 0-100
                .date(new Date(seconds))
                .metricType(metricType)
                .build();
      } else if (metricType.getInformationFormat() == InformationFormat.MB) {
        metric =
            Metric.builder()
                .value(new Random().nextDouble() * 1000) // just a random value
                .date(new Date(seconds))
                .metricType(metricType)
                .build();
      }
      metricRepository.save(metric);
    }
  }
}
