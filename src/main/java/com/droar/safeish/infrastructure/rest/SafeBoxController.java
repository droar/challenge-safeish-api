package com.droar.safeish.infrastructure.rest;

import javax.validation.Valid;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.droar.safeish.application.port.CreateSafeBoxUseCase;
import com.droar.safeish.application.port.ObtainSafeBoxItemsUseCase;
import com.droar.safeish.application.port.OpenSafeBoxUseCase;
import com.droar.safeish.application.port.UpdateSafeBoxItemsUseCase;
import com.droar.safeish.commons.Constants;
import com.droar.safeish.commons.CustomException;
import com.droar.safeish.commons.CustomExceptionResponse;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxItemsResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenDTO;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.PutSafeBoxItemsDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequestMapping("/safebox")
public class SafeBoxController {

  /** The create safe box user case. */
  @Autowired
  private CreateSafeBoxUseCase createSafeBoxUserCase;

  /** The open safe box use case. */
  @Autowired
  private OpenSafeBoxUseCase openSafeBoxUseCase;
  
  /** The get safe box items user case. */
  @Autowired
  private ObtainSafeBoxItemsUseCase obtainSafeBoxItemsUseCase;

  /** The update safe box items user case. */
  @Autowired
  private UpdateSafeBoxItemsUseCase updateSafeBoxItemsUseCase;
  
  /**
   * Post mapping for safebox endpoint.
   * This method is responsible for handing the POST -> /safebox requests.
   *
   * @param token the token
   * @param language the language
   * @param safeBoxPostDTO the safe box post DTO
   * @return the response entity
   */
  @PostMapping
  public ResponseEntity<?> postSafeBox(
      @RequestHeader(value = "Accept-Language", defaultValue = Constants.DEFAULT_LANGUAGE, required = false) String language,
      @RequestBody @Valid PostSafeBoxDTO safeBoxPostDTO) {
    log.info("[API SAFEBOX]: [POST] /safebox: Generating a new super secure safebox");

    ResponseEntity<PostSafeBoxResponseDTO> safeBoxGenerationResponse = null;

    try {
      PostSafeBoxResponseDTO safeBoxResponse = this.createSafeBoxUserCase.createSafeBox(safeBoxPostDTO);
      safeBoxGenerationResponse = ResponseEntity.ok(safeBoxResponse);

    } catch (CustomException e) {
      return ResponseEntity.status(e.getHttpStatusCode())
          .body(CustomExceptionResponse.builder()
          .status(String.valueOf(e.getHttpStatusCode().value()))
          .message(e.getMessage()).build());
    }

    return safeBoxGenerationResponse;
  }
  
  /**
   * Gets the safe box open token.
   * It will use a provided password and the safeboox provided uuid
   * 
   * This token will expire after 3 minutes
   *
   * @param language the language
   * @param safeBoxUuid the safe box uuid
   * @param safeBoxOpenDTO the safe box open DTO having the password
   * @return the safe box open token
   */
  @GetMapping(Constants.GET_SAFEBOX_OPEN_TOKEN)
  public ResponseEntity<?> getSafeBoxOpenToken(
      @RequestHeader(value = "Accept-Language", defaultValue = Constants.DEFAULT_LANGUAGE, required = false) String language,
      @PathVariable(value = "id") String safeBoxUuid,
      @RequestBody GetSafeBoxOpenDTO safeBoxOpenDTO) {
    log.info("[API SAFEBOX]: [GET] /safebox/{id}/open: Opening safebox with uuid -> " + safeBoxUuid);

    ResponseEntity<GetSafeBoxOpenResponseDTO> safeBoxOpenResponse = null;

    try {
      safeBoxOpenResponse = ResponseEntity.ok(this.openSafeBoxUseCase.openSafeBox(safeBoxUuid, safeBoxOpenDTO.getPassword()));
    } catch (CustomException e) {
      return ResponseEntity.status(e.getHttpStatusCode())
          .body(CustomExceptionResponse.builder()
          .status(String.valueOf(e.getHttpStatusCode().value()))
          .message(e.getMessage()).build());
    }

    return safeBoxOpenResponse;
  }

  /**
   * Gets the safe box items.
   * 
   * Get mapping for getting safebox items.
   * This method is responsible for handing the GET -> /safebox/{id}/items requests.
   *
   * @param token the token
   * @param language the language
   * @param safeBoxUuid the safe box uuid
   * @return the safe box items
   */
  @GetMapping(Constants.GET_SAFEBOX_ITEMS_BY_ID)
  public ResponseEntity<?> getSafeBoxItems(
      @RequestHeader("Authorization") String token,
      @RequestHeader(value = "Accept-Language", defaultValue = Constants.DEFAULT_LANGUAGE, required = false) String language,
      @PathVariable(value = "id") String safeBoxUuid) {
    log.info("[API SAFEBOX]: [GET] /safebox/{id}/items: Getting safebox items by uuid -> " + safeBoxUuid);

    ResponseEntity<GetSafeBoxItemsResponseDTO> safeBoxObtainItemsResponse = null;

    try {
      safeBoxObtainItemsResponse = ResponseEntity.ok(this.obtainSafeBoxItemsUseCase.obtainSafeBoxItems(safeBoxUuid));
    } catch (CustomException e) {
      return ResponseEntity.status(e.getHttpStatusCode())
          .body(CustomExceptionResponse.builder()
          .status(String.valueOf(e.getHttpStatusCode().value()))
          .message(e.getMessage()).build());
    }

    return safeBoxObtainItemsResponse;
  }

  /**
   * Puts the safe box items into the safebox
   * 
   * Put mapping for adding safebox items.
   * This method is responsible for handing the POT -> /safebox/{id}/items requests.
   *
   * @param token the token
   * @param language the language
   * @param safeBoxUuid the safe box uuid
   * @param safeBoxPutDTO the safe box put DTO
   * @return the response entity
   */
  @PutMapping(Constants.PUT_SAFEBOX_ITEMS_BY_ID)
  public ResponseEntity<?> putSafeBoxItems(
      @RequestHeader("Authorization") String token,
      @RequestHeader(value = "Accept-Language", defaultValue = Constants.DEFAULT_LANGUAGE, required = false) String language,
      @PathVariable(value = "id") String safeBoxUuid, @RequestBody PutSafeBoxItemsDTO safeBoxPutDTO) {
    log.info("[API SAFEBOX]: [PUT] /safebox/{id}/items: Adding new items to the safebox -> " + safeBoxUuid);

    ResponseEntity<?> safeBoxUpdateResponse = null;

    try {
      Boolean hasBeenUpdated = this.updateSafeBoxItemsUseCase.updateSafeBoxItems(safeBoxUuid, safeBoxPutDTO);

      if (BooleanUtils.isTrue(hasBeenUpdated)) {
        safeBoxUpdateResponse = ResponseEntity.status(HttpStatus.OK).build();
      }
    } catch (CustomException e) {
      return ResponseEntity.status(e.getHttpStatusCode())
          .body(CustomExceptionResponse.builder()
          .status(String.valueOf(e.getHttpStatusCode().value()))
          .message(e.getMessage()).build());
    }

    return safeBoxUpdateResponse;
  }
}
