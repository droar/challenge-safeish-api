package com.droar.safeish.application.port.out;

import com.droar.safeish.domain.SafeBox;

/**
 * The Interface CreateSafeBoxPort, this class
 * provides a port for the safe box domain entity to be
 * consumed by the adapters.
 */
public interface CreateSafeBoxPort {

  /**
   * Creates a new safe box, it will use the pertinent repository
   * asociated as adapter.
   *
   * @return the safe box
   */
  public SafeBox createSafeBox(SafeBox safeBoxToBeCreated);
  
  /**
   * Validate if the uuid provided exists on another safebox and its really unique
   *
   * @param uuidToValidate the uuid to validate
   * @return true if the uuid is already exists
   */
  public Boolean safeBoxByUuidExists(String uuidToValidate);
  
  /**
   * Validate if the uuid provided exists on another safebox and its really unique
   *
   * @param uuidToValidate the uuid to validate
   * @return true if the uuid is already exists
   */
  
  /**
   * Gets the safe box domain with its uuid provided
   * 
   * @param safeBoxUuid
   * @return the safebox itself
   */
  public SafeBox obtainSafeBoxByUuid(String safeBoxUuid);
}
