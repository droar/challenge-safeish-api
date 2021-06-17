package com.droar.safeish.application.port;

import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxResponseDTO;

/**
 * The Interface CreateSafeBoxUseCase.
 */
public interface CreateSafeBoxUseCase {
  
  /**
   * Creates a new safeBox, it will autogenerate a new unique Uuid each time, 
   * returns the response DTO back to the view (in this case controller)
   *
   * @param postSafeBoxDTO the post safe box DTO
   * @return the post safe box response DTO
   */
  PostSafeBoxResponseDTO createSafeBox(PostSafeBoxDTO postSafeBoxDTO); 

}
