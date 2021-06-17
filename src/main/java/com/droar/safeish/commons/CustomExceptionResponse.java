package com.droar.safeish.commons;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import lombok.Builder;
import lombok.Getter;

/**
 * Custom exception response.
 *
 * @author droar
 */
@Builder
@Getter
public class CustomExceptionResponse {

  /** The formatter. */
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd / HH:mm:ss");

  /** The timestamp. */
  private final String timestamp = sdf.format(new Timestamp(System.currentTimeMillis()));

  /** The message. */
  private String status;

  /** The message. */
  private String message;


  @Override
  public String toString() {
    return "[" + timestamp + "][" + status + "][" + message;
  }
}
