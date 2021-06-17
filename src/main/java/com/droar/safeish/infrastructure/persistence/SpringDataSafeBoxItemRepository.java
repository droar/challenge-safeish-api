package com.droar.safeish.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxItemJpaEntity;

/**
 * Interface SpringDataSafeBoxItemRepository.
 * 
 * This interface extends the jpa repository of spring data, it will give us data access to safebox
 * item jpa layer
 * 
 * @author droar
 *
 */
interface SpringDataSafeBoxItemRepository extends JpaRepository<SafeBoxItemJpaEntity, Long> {

  /**
   * Finds all the safe box items asociated to a safebox uuid
   *
   * @param uuid
   * @return the list of safebox items related to the safebox uuid
   */
  public List<SafeBoxItemJpaEntity> findBySafeBoxUuid(String safeBoxUuid);
}
