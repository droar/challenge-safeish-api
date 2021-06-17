package com.droar.safeish.application.port.out;

/**
 * The Interface OpenSafeBoxPort, this class
 * provides a port for the safe box domain entity to be
 * consumed by the adapters. Specificaly to handle the open safe box operations
 * 
 */
public interface OpenSafeBoxPort {

  /**
   * Will try to find a safebox with its uuid and password
   * 
   * @param safeBoxUuid
   * @param password
   * @return true if the safebox exists with provided uuid and password
   */
  public Boolean existsSafeBoxWithPassword(String safeBoxUuid, String password);
  
  /**
   * Block or sums wrong attepts to safe box, this operation will be launched
   * when safe box gets blocked after n attempts
   * or when a fail attept is detected
   * 
   * @param uuidSafeBoxToBlock the uuid safe box to block
   */
  public void attemptToBlockSafeBox(String uuidSafeBoxToBlock);
  
  /**
   * Returns if the provided safebox exists and its blocked
   * 
   * @param uuidSafeBoxToBlock
   * @return true if the safebox is blocked
   */
  public Boolean isSafeBoxBlocked(String uuidSafeBoxToBlock);
}
