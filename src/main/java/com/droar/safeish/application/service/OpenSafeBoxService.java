package com.droar.safeish.application.service;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.droar.safeish.application.port.OpenSafeBoxUseCase;
import com.droar.safeish.application.port.out.CreateSafeBoxPort;
import com.droar.safeish.application.port.out.OpenSafeBoxPort;
import com.droar.safeish.commons.CustomException;
import com.droar.safeish.infrastructure.configuration.JwtUserDetailsService;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenResponseDTO;
import com.droar.safeish.infrastructure.rest.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class OpenSafeBoxService.
 * 
 * This class acts as safe box application service, it will consume the open safebox port to
 * open an existing safe box.
 * 
 * @author droar
 *
 */
@Service
@Slf4j
public class OpenSafeBoxService implements OpenSafeBoxUseCase {

  /** The open safe box port. */
  @Autowired
  private OpenSafeBoxPort openSafeBoxPort;
  
  /** The create safe box port. */
  @Autowired
  private CreateSafeBoxPort createSafeBoxPort;
  
  /** The message resource. */
  @Autowired
  private ReloadableResourceBundleMessageSource messageSource;
  
  /** The authentication manager. */
  @Autowired
  private AuthenticationManager authenticationManager;

  /** The jwt token util. */
  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  /** The user details service. */
  @Autowired
  private JwtUserDetailsService userDetailsService;
  
  @Override
  public GetSafeBoxOpenResponseDTO openSafeBox(String safeBoxUuid, String password) {
    GetSafeBoxOpenResponseDTO responseDTO = new GetSafeBoxOpenResponseDTO();
    
    // If any parameter is blank, we will throw malformed exception
    if (StringUtils.isAnyBlank(safeBoxUuid, password)) {
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
    
    // If the safebox exists, but doesnt match with its password, we will sum a block attempt, 
    // and we will return an exception
    if(BooleanUtils.isFalse(this.openSafeBoxPort.existsSafeBoxWithPassword(safeBoxUuid, password))) {
      this.openSafeBoxPort.attemptToBlockSafeBox(safeBoxUuid);
      
      throw new CustomException(HttpStatus.NOT_FOUND, this.messageSource.getMessage("http.incorrect_password", null, LocaleContextHolder.getLocale()));
    }
    
    try {
      // Auth of a safebox trough the spring security methods
      this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(safeBoxUuid, password));
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(safeBoxUuid);
      
      // We will return an auth token when opening the safebox succeds
      responseDTO.setToken(jwtTokenUtil.generateToken(userDetails)); 
    } catch (Exception e) {
      log.error("> Error happened when trying to open safebox: " + e.getMessage());
      // If no controlled error happens, we will throw unexpected api error
      throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, this.messageSource.getMessage("http.internal_server_error", null, LocaleContextHolder.getLocale()));
    }

    return responseDTO;
  }

}
