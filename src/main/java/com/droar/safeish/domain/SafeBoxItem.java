package com.droar.safeish.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Safeboxitem domain
 * 
 * @author droar
 *
 */
@Data
@AllArgsConstructor
public class SafeBoxItem {

  /** The safe box uuid. */
  private String safeBoxUuid;

  /** The item value. */
  private String itemValue;
}
