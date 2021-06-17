package com.droar.safeish.infrastructure.persistence;

import org.springframework.stereotype.Component;
import com.droar.safeish.domain.SafeBoxItem;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxItemJpaEntity;

/**
 * The Class SafeBoxItemMapper - It helps mapping from domain to jpa entity or vice versa
 * 
 */
@Component
class SafeBoxItemMapper {
  
  /**
   * Map to domain entity.
   *
   * @param safeBoxItemJpaEntity the safe box jpa entity
   * @return the safe box
   */
  SafeBoxItem mapToDomainEntity(SafeBoxItemJpaEntity safeBoxItemJpaEntity) {
    return new SafeBoxItem(safeBoxItemJpaEntity.getSafeBoxUuid(), 
        safeBoxItemJpaEntity.getItemValue());
  }

  /**
   * Map to jpa entity.
   *
   * @param domainSafeBoxItem the domain safe box
   * @return the safe box jpa entity
   */
  SafeBoxItemJpaEntity mapToJpaEntity(SafeBoxItem domainSafeBoxItem) {
    return new SafeBoxItemJpaEntity(domainSafeBoxItem.getSafeBoxUuid(), 
        domainSafeBoxItem.getItemValue());
  }
}
