package com.droar.safeish.application.service;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.droar.safeish.application.port.ObtainSafeBoxItemsUseCase;
import com.droar.safeish.application.port.out.CreateSafeBoxPort;
import com.droar.safeish.application.port.out.LoadSafeBoxItemsPort;
import com.droar.safeish.application.port.out.OpenSafeBoxPort;
import com.droar.safeish.commons.CustomException;
import com.droar.safeish.domain.SafeBoxItem;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxItemsResponseDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ObtainSafeBoxItemsService.
 * 
 * This class acts as safe box application service, it will consume the obtain safe box
 * items port to get the items of the safebox asociated.
 * 
 * @author droar
 *
 */
@Service
@Slf4j
public class ObtainSafeBoxItemsService implements ObtainSafeBoxItemsUseCase {

  /** The load safe box items port. */
  @Autowired
  private LoadSafeBoxItemsPort loadSafeBoxItemsPort;
  
  /** The create safe box port. */
  @Autowired
  private CreateSafeBoxPort createSafeBoxPort;
  
  /** The open safe box port. */
  @Autowired
  private OpenSafeBoxPort openSafeBoxPort;
  
  /** The message resource. */
  @Autowired
  private ReloadableResourceBundleMessageSource messageSource;

  @Override
  public GetSafeBoxItemsResponseDTO obtainSafeBoxItems(String safeBoxUuid) {
    GetSafeBoxItemsResponseDTO responseDTO = new GetSafeBoxItemsResponseDTO();

    // If any parameter is blank, we will throw malformed exception
    if (StringUtils.isBlank(safeBoxUuid)) {
      throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, this.messageSource.getMessage("http.malformed_error", null, LocaleContextHolder.getLocale()));
    }
    
    // If the safebox doesnt exist, we will throw an error
    if(BooleanUtils.isFalse(this.createSafeBoxPort.safeBoxByUuidExists(safeBoxUuid))) {
      throw new CustomException(HttpStatus.NOT_FOUND, this.messageSource.getMessage("http.inexistent_safebox", null, LocaleContextHolder.getLocale()));
    }
    
    // If the safebox is blocked, we will throw an error
    if (BooleanUtils.isTrue(this.openSafeBoxPort.isSafeBoxBlocked(safeBoxUuid))) {
      throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, this.messageSource.getMessage("http.locked_safebox", null, LocaleContextHolder.getLocale()));
    }
    
    try {
      List<SafeBoxItem> lstDomainSafeBoxItems = this.loadSafeBoxItemsPort.loadSafeBoxItems(safeBoxUuid);

      if (CollectionUtils.isNotEmpty(lstDomainSafeBoxItems)) {
        log.info("> Safebox items located, returning: " + lstDomainSafeBoxItems.size() + " items stored.");
        responseDTO.setLstItemValues(lstDomainSafeBoxItems.stream().map(SafeBoxItem::getItemValue).collect(Collectors.toList()));
      }

    } catch (Exception e) {
      log.error("> Error happened when trying to fetch the safebox items " + e.getMessage());

      // If no controlled error happens, we will throw unexpected api error
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, this.messageSource.getMessage("http.internal_server_error", null, LocaleContextHolder.getLocale()));
    }

    return responseDTO;
  }

}
