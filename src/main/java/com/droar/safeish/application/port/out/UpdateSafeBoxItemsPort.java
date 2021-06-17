package com.droar.safeish.application.port.out;

import java.util.List;
import com.droar.safeish.domain.SafeBoxItem;

/**
 * The Interface UpdateSafeBoxItemsPort, this class
 * provides a port for the safe box item domain entity to be
 * consumed by the adapters.
 * 
 */
public interface UpdateSafeBoxItemsPort {

  /**
   * Update safe box items sociated to an existing and provided uuid.
   * 
   * @param lstDomainSafeBoxItems
   * @return
   */
  public Boolean updateSafeBoxItems(String safeBoxUuid, List<SafeBoxItem> lstDomainSafeBoxItems);
}
