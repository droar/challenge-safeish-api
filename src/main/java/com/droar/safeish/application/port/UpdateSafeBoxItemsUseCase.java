package com.droar.safeish.application.port;

import com.droar.safeish.infrastructure.rest.dto.PutSafeBoxItemsDTO;

/**
 * The Interface UpdateSafeBoxItemsUseCase.
 */
public interface UpdateSafeBoxItemsUseCase {
  
  /**
   * Update provided safebox uuid with the safebox items provided.
   * 
   * @param safeBoxUuid
   * @param safeBoxItems
   * @return true if the safebox items got updated
   */
  Boolean updateSafeBoxItems(String safeBoxUuid, PutSafeBoxItemsDTO safeBoxItems); 

}
