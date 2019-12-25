package com.ciprian.cusmuliuc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CusmuliucApplication {

  public static void main(String[] args) {
    SpringApplication.run(CusmuliucApplication.class, args);
  }
}
