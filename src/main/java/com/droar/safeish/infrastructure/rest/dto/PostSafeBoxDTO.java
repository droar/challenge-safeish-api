package com.droar.safeish.infrastructure.rest.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.droar.safeish.infrastructure.rest.validation.ValidPassword;
import lombok.Data;

/**
 * The Class PostSafeBoxDTO.
 */
@Data
public class PostSafeBoxDTO implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 590476756482003276L;
  
  /** The name. */
  @JsonProperty(value = "name")
  private String name;
  
  /** The password, it uses a custom validator to make sure it will stay strong*/
  @ValidPassword
  @JsonProperty(value = "password")
  private String password;
}
