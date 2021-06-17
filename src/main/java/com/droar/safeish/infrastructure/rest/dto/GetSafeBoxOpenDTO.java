package com.droar.safeish.infrastructure.rest.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The Class GetSafeBoxOpenDTO.
 * 
 * This dto is needed, and wasnt provided on the yml expecification
 */
@Data
public class GetSafeBoxOpenDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 590476756482003276L;
  
  /** The password of the safebox*/
  @JsonProperty(value = "password")
  private String password;
}
