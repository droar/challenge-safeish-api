package com.droar.safeish.infrastructure.rest.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The Class GetSafeBoxOpenResponseDTO.
 * 
 * will return the token auth
 */
@Data
public class GetSafeBoxOpenResponseDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 590476756482003276L;
  
  /** The token. */
  @JsonProperty(value = "token")
  private String token;
}
