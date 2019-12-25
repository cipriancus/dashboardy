package com.ciprian.cusmuliuc.exception;

/**
 * A TechnicalException is an exception that happens during program execution and signals the user
 * something is not ok, e.g: he put wrong id format
 */
public class TechnicalException extends RuntimeException {
  public TechnicalException(String errorMessage) {
    super(errorMessage);
  }
}
