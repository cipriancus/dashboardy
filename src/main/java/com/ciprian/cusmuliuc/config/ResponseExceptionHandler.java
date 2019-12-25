package com.ciprian.cusmuliuc.config;

import com.ciprian.cusmuliuc.dto.exception.ApiErrorDTO;
import com.ciprian.cusmuliuc.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResponseExceptionHandler {
  Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);

  /**
   * Every technical exeption thrown by app will be converted to a DTO as an error response
   *
   * @param exception
   * @return DTO response
   */
  @ExceptionHandler(TechnicalException.class)
  public @ResponseBody ResponseEntity<ApiErrorDTO> handleTechnicalException(
      TechnicalException exception) {
    logger.error(exception.getMessage(), exception);
    return new ResponseEntity<>(
        new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
