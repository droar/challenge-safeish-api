package com.droar.safeish.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxJpaEntity;

/**
 * Interface SpringDataSafeBoxRepository.
 * 
 * This interface extends the jpa repository of spring data, it will
 * give us data access to safebox jpa layer
 * 
 * @author droar
 *
 */
interface SpringDataSafeBoxRepository extends JpaRepository<SafeBoxJpaEntity, Long> {
  
  /**
   * Exists by uuid.
   *
   * @param uuid the uuid
   * @return the boolean
   */
  public Boolean existsByUuid(String uuid);
  
  /**
   * Find safebox by uuid
   * 
   * @param uuid
   * @return the safebox entity
   */
  public SafeBoxJpaEntity findByUuid(String uuid);
}
