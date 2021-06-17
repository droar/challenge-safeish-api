package com.droar.safeish.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The Class GetSafeBoxItemsDTO.
 */
@Data
public class GetSafeBoxItemsResponseDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 590476756482003276L;
  
  /** The lst item values. */
  @JsonProperty(value = "items")
  private List<String> lstItemValues;
}
