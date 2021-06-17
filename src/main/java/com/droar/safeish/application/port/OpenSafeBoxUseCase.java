package com.droar.safeish.application.port;

import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenResponseDTO;

/**
 * The Interface OpenSafeBoxUseCase.
 */
public interface OpenSafeBoxUseCase {

  /**
   * Open (or try to) a safebox with the provided uuid and password
   * 
   * @param safeBoxUuid
   * @param password
   * @return
   */
  GetSafeBoxOpenResponseDTO openSafeBox(String safeBoxUuid, String password);

}
