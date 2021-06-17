package com.droar.safeish.infrastructure.persistence;

import org.springframework.stereotype.Component;
import com.droar.safeish.domain.SafeBox;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxJpaEntity;

/**
 * The Class SafeBoxMapper - It helps mapping from domain to jpa entity or vice versa
 * 
 */
@Component
class SafeBoxMapper {
  
  /**
   * Map to domain entity.
   *
   * @param safeBoxJpaEntity the safe box jpa entity
   * @return the safe box
   */
  SafeBox mapToDomainEntity(SafeBoxJpaEntity safeBoxJpaEntity) {
    return new SafeBox(safeBoxJpaEntity.getUuid(), 
        safeBoxJpaEntity.getName(), 
        safeBoxJpaEntity.getPassword());
  }

  /**
   * Map to jpa entity.
   *
   * @param domainSafeBox the domain safe box
   * @return the safe box jpa entity
   */
  SafeBoxJpaEntity mapToJpaEntity(SafeBox domainSafeBox) {
    return new SafeBoxJpaEntity(domainSafeBox.getUuid(), 
        domainSafeBox.getName(), 
        domainSafeBox.getPassword());
  }
}
