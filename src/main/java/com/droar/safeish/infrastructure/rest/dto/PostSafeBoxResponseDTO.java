package com.droar.safeish.infrastructure.rest.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The Class PostSafeBoxResponseDTO.
 */
@Data
public class PostSafeBoxResponseDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 590476756482003276L;
  
  /** The id. */
  @JsonProperty(value = "id")
  private String uuid;
}
