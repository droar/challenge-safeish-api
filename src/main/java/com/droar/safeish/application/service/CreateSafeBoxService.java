package com.droar.safeish.application.service;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.droar.safeish.application.port.CreateSafeBoxUseCase;
import com.droar.safeish.application.port.out.CreateSafeBoxPort;
import com.droar.safeish.commons.CustomException;
import com.droar.safeish.domain.SafeBox;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxResponseDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CreateSafeBoxService.
 * 
 * This class acts as safe box application service, it will consume the safe box creation port to
 * generate a new safe box.
 * 
 * @author droar
 *
 */
@Service
@Slf4j
public class CreateSafeBoxService implements CreateSafeBoxUseCase {

  /** The create safe box port. */
  @Autowired
  private CreateSafeBoxPort createSafeBoxPort;

  /** The message resource. */
  @Autowired
  private ReloadableResourceBundleMessageSource messageSource;

  @Override
  public PostSafeBoxResponseDTO createSafeBox(PostSafeBoxDTO postSafeBoxDTO) {
    PostSafeBoxResponseDTO responseDTO = new PostSafeBoxResponseDTO();

    // If any parameter is blank, we will throw malformed exception
    if (StringUtils.isAnyBlank(postSafeBoxDTO.getName(), postSafeBoxDTO.getPassword())) {
      throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, this.messageSource.getMessage("http.malformed_error", null, LocaleContextHolder.getLocale()));
    }

    try {
      // We will check uuids until we get an unused one, this way we dont have to throw an error
      Boolean validUuid;
      String generatedRandomUuid;

      do {
        log.info("> Generating a new random uuid for your safebox");
        generatedRandomUuid = UUID.randomUUID().toString();
        validUuid = this.createSafeBoxPort.safeBoxByUuidExists(generatedRandomUuid);
      } while (validUuid == Boolean.TRUE);

      // When we have an unused uuid, we will use it, safebox will not be blocked at creation
      SafeBox generatedSafeBox = this.createSafeBoxPort.createSafeBox(new SafeBox(generatedRandomUuid, postSafeBoxDTO.getName(), postSafeBoxDTO.getPassword()));
      responseDTO.setUuid(generatedSafeBox.getUuid());
    } catch (Exception e) {
      log.error("> Error happened when trying to save a new safebox: " + e.getMessage());
      // If no controlled error happens, we will throw unexpected api error
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, this.messageSource.getMessage("http.internal_server_error", null, LocaleContextHolder.getLocale()));
    }

    return responseDTO;
  }

}
