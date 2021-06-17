package com.droar.safeish.application.port.out;

import java.util.List;
import com.droar.safeish.domain.SafeBoxItem;

/**
 * The Interface LoadSafeBoxItemsPort, this class
 * provides a port for the safe box item domain entity to be
 * consumed by the adapters.
 */
public interface LoadSafeBoxItemsPort {
  
  /**
   * Load safe box items, for this it uses a existent safeBoxUuid
   *
   * @return the list
   */
  public List<SafeBoxItem> loadSafeBoxItems(String safeBoxUuid);
}
