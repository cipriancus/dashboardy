package com.ciprian.cusmuliuc.dto.exception;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/** This error dto is used in the exception handler */
public class ApiErrorDTO {
  private int code;
  private String description;
}
