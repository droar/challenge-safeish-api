package com.droar.safeish.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.droar.safeish.application.port.out.CreateSafeBoxPort;
import com.droar.safeish.application.port.out.LoadSafeBoxItemsPort;
import com.droar.safeish.application.port.out.OpenSafeBoxPort;
import com.droar.safeish.application.port.out.UpdateSafeBoxItemsPort;
import com.droar.safeish.commons.CipherUtils;
import com.droar.safeish.domain.SafeBox;
import com.droar.safeish.domain.SafeBoxItem;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxItemJpaEntity;
import com.droar.safeish.infrastructure.persistence.entity.SafeBoxJpaEntity;

/**
 * The class SafeBoxPersistenceAdapter.
 * 
 * This class serves as a persistence adapter for the safebox operations
 * It is actually feeded by a spring data repository (using spring) but it could
 * be replaced as needed.
 * 
 */
@Repository
class SafeBoxPersistenceAdapter implements CreateSafeBoxPort, LoadSafeBoxItemsPort, UpdateSafeBoxItemsPort, OpenSafeBoxPort {

  /** The safe box repository. */
  @Autowired
  private SpringDataSafeBoxRepository safeBoxRepository;
  
  /** The safe box items repository. */
  @Autowired
  private SpringDataSafeBoxItemRepository safeBoxItemRepository;
  
  /** The safe box mapper. */
  @Autowired
  private SafeBoxMapper safeBoxMapper;
  
  /** The safe box item mapper. */
  @Autowired
  private SafeBoxItemMapper safeBoxItemMapper;
  
  /** The cipher utils. */
  @Autowired
  private CipherUtils cipherUtils;
  
  /** The cipher utils. */
  @Value("${safebox.max.open.attempts}")
  private Integer safeBlockMaxOpenAttempts;
  
  @Override
  public SafeBox createSafeBox(SafeBox safeBoxToBeCreated) {
    // We will also encrypt the password, we want it to be really safe for the user.
    safeBoxToBeCreated.setPassword(this.cipherUtils.encryptPassword(safeBoxToBeCreated.getPassword()));
    
    SafeBoxJpaEntity safeBoxJpaSavedEntity = this.safeBoxRepository.save(this.safeBoxMapper.mapToJpaEntity(safeBoxToBeCreated));
    return this.safeBoxMapper.mapToDomainEntity(safeBoxJpaSavedEntity);
  }
  
  @Override
  public Boolean safeBoxByUuidExists(String uuidToValidate) {
    return this.safeBoxRepository.existsByUuid(uuidToValidate);
  }
  

  @Override
  public SafeBox obtainSafeBoxByUuid(String safeBoxUuid) {
    return this.safeBoxMapper.mapToDomainEntity(this.safeBoxRepository.findByUuid(safeBoxUuid));
  }

  @Override
  public List<SafeBoxItem> loadSafeBoxItems(String safeBoxUuid) {
    List<SafeBoxItem> lstDomainSafeBoxItems = new ArrayList<>();
    
    List<SafeBoxItemJpaEntity> lstJpaSafeBoxItems =  this.safeBoxItemRepository.findBySafeBoxUuid(safeBoxUuid);
    
    // We need to decipher the safe box items
    lstJpaSafeBoxItems = lstJpaSafeBoxItems.stream()
        .map(j -> new SafeBoxItemJpaEntity(j.getSafeBoxUuid(), this.cipherUtils.decryptString(j.getItemValue()))).collect(Collectors.toList());
    
    if(CollectionUtils.isNotEmpty(lstJpaSafeBoxItems)) {
      lstDomainSafeBoxItems = lstJpaSafeBoxItems.stream().map(ji -> this.safeBoxItemMapper.mapToDomainEntity(ji)).collect(Collectors.toList());
    }
    
    return lstDomainSafeBoxItems;
  }

  @Override
  public Boolean updateSafeBoxItems(String safeBoxUuid, List<SafeBoxItem> lstDomainSafeBoxItems) {
    List<SafeBoxItemJpaEntity> lstSafeBoxOriginalItems = this.safeBoxItemRepository.findBySafeBoxUuid(safeBoxUuid);
    
    // We need to decrypt the safe box original items
    List<String> lstItemValues = lstSafeBoxOriginalItems.stream().map(sbi -> this.cipherUtils.decryptString(sbi.getItemValue())).collect(Collectors.toList());
    
    // Filter the safebox items, to only add new ones, existent ones will be ignored (for now)
    List<SafeBoxItemJpaEntity> lstJpaSafeBoxItems = lstDomainSafeBoxItems.stream()
        .filter(si -> BooleanUtils.isFalse(lstItemValues.contains(si.getItemValue())))
        .map(si -> new SafeBoxItemJpaEntity(si.getSafeBoxUuid(), this.cipherUtils.encryptString(si.getItemValue())))
        .collect(Collectors.toList());
    
    return CollectionUtils.isNotEmpty(this.safeBoxItemRepository.saveAllAndFlush(lstJpaSafeBoxItems));
  }

  @Override
  public Boolean existsSafeBoxWithPassword(String safeBoxUuid, String password) {
    // Database passwords are encrypted, so we will have to decrypt it to see if it matches, remember, always secure.
    SafeBoxJpaEntity safeBox = this.safeBoxRepository.findByUuid(safeBoxUuid);
    return this.cipherUtils.passwordMatchesEncrypted(password, safeBox.getPassword());
  }

  @Override
  public void attemptToBlockSafeBox(String uuidSafeBoxToBlock) {
    SafeBoxJpaEntity safeBoxEntity = this.safeBoxRepository.findByUuid(uuidSafeBoxToBlock);
    
    if (safeBoxEntity != null) {
      // If open attempts is minor to max open attempts, we will sum one to the equation
      safeBoxEntity.setNumOpenAttempts(safeBoxEntity.getNumOpenAttempts() + 1);
      
      if (safeBoxEntity.getNumOpenAttempts() >= this.safeBlockMaxOpenAttempts) {
        // If its equal or bigger, we will block the safe box
        safeBoxEntity.setBlocked(Boolean.TRUE);
      }
      this.safeBoxRepository.save(safeBoxEntity);
    }
  }

  @Override
  public Boolean isSafeBoxBlocked(String uuidSafeBoxToBlock) {
    return this.safeBoxRepository.findByUuid(uuidSafeBoxToBlock).getBlocked();
  }

}
