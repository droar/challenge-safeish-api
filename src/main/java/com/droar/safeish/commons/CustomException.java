package com.droar.safeish.commons;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Custom exception class.
 *
 * @author droar
 */
public class CustomException extends RuntimeException {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The exception code. */
  @Getter
  private final HttpStatus httpStatusCode;

  /**
   * Instantiates a new custom exception.
   *
   * @param exceptionCode the exception code
   * @param errorMessage the error message
   * @param err the err
   */
  public CustomException(HttpStatus exceptionCode, String errorMessage, Throwable err) {
    super(errorMessage, err);
    this.httpStatusCode = exceptionCode;
  }

  /**
   * Instantiates a new custom exception.
   *
   * @param exceptionCode the exception code
   * @param errorMessage the error message
   */
  public CustomException(HttpStatus exceptionCode, String errorMessage) {
    super(errorMessage);
    this.httpStatusCode = exceptionCode;
  }
}
