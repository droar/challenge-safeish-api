package com.droar.safeish.application.port;

import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxItemsResponseDTO;

/**
 * The Interface ObtainSafeBoxItemsUseCase.
 */
public interface ObtainSafeBoxItemsUseCase {
  
  /**
   * Obtains the safe box items by the safebox uuid provided.
   *
   * @param safeBoxUuid the safe box uuid
   * @return the gets the safe box items response DTO
   */
  GetSafeBoxItemsResponseDTO obtainSafeBoxItems(String safeBoxUuid); 
}
