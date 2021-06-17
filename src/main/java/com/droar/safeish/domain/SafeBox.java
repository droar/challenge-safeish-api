package com.droar.safeish.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Safebox class, it holds the domain for a very safe and secure box.
 * 
 * @author droar
 *
 */
@Data
@AllArgsConstructor
public class SafeBox {

  /** The uuid. */
  private String uuid;

  /** The safebox name. */
  private String name;

  /** The safebox password. */
  private String password;
}
