/*
 * 
 */
package com.droar.safeish.infrastructure.rest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.droar.safeish.application.port.CreateSafeBoxUseCase;
import com.droar.safeish.application.port.ObtainSafeBoxItemsUseCase;
import com.droar.safeish.application.port.OpenSafeBoxUseCase;
import com.droar.safeish.application.port.UpdateSafeBoxItemsUseCase;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxItemsResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenDTO;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxResponseDTO;
import com.droar.safeish.infrastructure.rest.dto.PutSafeBoxItemsDTO;

/**
 * The Class SafeBoxControllerTest.
 */
@DisplayName("Controller rest test use cases")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SafeBoxControllerTest {

  /** The mock mvc. */
  @Autowired
  private MockMvc mockMvc;

  /** The create safe box use case mock. */
  @MockBean
  private CreateSafeBoxUseCase createSafeBoxUseCaseMock;
  
  /** The open safe box use case mock. */
  @MockBean
  private OpenSafeBoxUseCase openSafeBoxUseCaseMock;
  
  /** The get safe box items user case mock. */
  @MockBean
  private ObtainSafeBoxItemsUseCase obtainSafeBoxItemsUseCaseMock;

  /** The update safe box items user case mock. */
  @MockBean
  private UpdateSafeBoxItemsUseCase updateSafeBoxItemsUseCaseMock;
  
  @Test
  @DisplayName(value = "Succeeds when creates a safebox correctly")
  void givenNameAndPasswordWhenCreatingSafeBoxWithValidPasswordAndNameThenSucceed() throws Exception {
    PostSafeBoxDTO createSafeBoxMockDTO = new PostSafeBoxDTO();
    createSafeBoxMockDTO.setName("Testing creation SafeBox 1");
    createSafeBoxMockDTO.setPassword("S3cur3Passw0rd##");
    
    PostSafeBoxResponseDTO responseSafeBoxCreateMockDTO = new PostSafeBoxResponseDTO();
    responseSafeBoxCreateMockDTO.setUuid("c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab");
    
    when(this.createSafeBoxUseCaseMock.createSafeBox(createSafeBoxMockDTO)).thenReturn(responseSafeBoxCreateMockDTO);
    
    this.mockMvc.perform(post("/safebox")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsBytes(createSafeBoxMockDTO))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(responseSafeBoxCreateMockDTO.getUuid())));
  }
  
  @Test
  @DisplayName(value = "Succeeds when opening a safebox with correct uuid and existing valid password, returns the valid token")
  void givenUuidAndSafePasswordWhenExistingUuidAndPasswordThenSucceed() throws Exception {
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";
    GetSafeBoxOpenDTO getSafeBoxOpenDTO = new GetSafeBoxOpenDTO();
    getSafeBoxOpenDTO.setPassword("S3cur3Passw0rd##");
    
    GetSafeBoxOpenResponseDTO getSafeBoxOpenResponseDTO = new GetSafeBoxOpenResponseDTO();
    getSafeBoxOpenResponseDTO.setToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMGM5ZGNjNy0xYmYzLTRjOTMtYmM5NC1lNDUwMDJlZGQ3YWIiLCJleHAiOjE2MjM4NjIzMzAs"
        + "ImlhdCI6MTYyMzg2MjE1MH0.J0HinJ2GeIrFc-8CE51sPbePkyflT4w5oaNW7qngUYlIY6NbKrmGALW1qkVPyqX7Ibt7hZgoEHLF-yskT6uy9A");
    
    when(this.openSafeBoxUseCaseMock.openSafeBox(safeBoxUuid, getSafeBoxOpenDTO.getPassword())).thenReturn(getSafeBoxOpenResponseDTO);
    
    this.mockMvc.perform(get("/safebox/"+safeBoxUuid+"/open")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsBytes(getSafeBoxOpenDTO))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(getSafeBoxOpenResponseDTO.getToken())));
  }
  
  @Test
  @DisplayName(value = "Succeeds when opening a safebox and retrieving its items")
  @WithMockUser(username = "safeish", roles={"ADMIN"})
  void givenUuidWhenExistingSafeBoxAndHavingItemsThenSucceed() throws Exception {
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";
    
    GetSafeBoxItemsResponseDTO getSafeBoxItemsResponseDTO = new GetSafeBoxItemsResponseDTO();
    List<String> lstItems = Arrays.asList("Item 1", "Item 2");
    getSafeBoxItemsResponseDTO.setLstItemValues(lstItems);
    
    when(this.obtainSafeBoxItemsUseCaseMock.obtainSafeBoxItems(safeBoxUuid)).thenReturn(getSafeBoxItemsResponseDTO);
    
    this.mockMvc.perform(get("/safebox/"+safeBoxUuid+"/items")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMGM5ZGNjNy0xYmYzLTRjOTMtYmM5NC1lNDUwMDJlZGQ3YWIiLCJleHAiOjE2MjM4NjIzMzAs"
            + "ImlhdCI6MTYyMzg2MjE1MH0.J0HinJ2GeIrFc-8CE51sPbePkyflT4w5oaNW7qngUYlIY6NbKrmGALW1qkVPyqX7Ibt7hZgoEHLF-yskT6uy9A")
        .content(new ObjectMapper().writeValueAsBytes(safeBoxUuid))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Item 1")));
  }
  
  @Test
  @DisplayName(value = "Succeeds when opening a safebox and adding items to it")
  @WithMockUser(username = "safeish", roles={"ADMIN"})
  void givenUuidAndSafeItemsWhenExistingUuidThenSucceed() throws Exception {
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";
    PutSafeBoxItemsDTO putSafeBoxItemsDTO = new PutSafeBoxItemsDTO();
    
    List<String> lstItems = Arrays.asList("Item 1", "Item 2");
    putSafeBoxItemsDTO.setLstItemsToAdd(lstItems);
    
    when(this.updateSafeBoxItemsUseCaseMock.updateSafeBoxItems(safeBoxUuid, putSafeBoxItemsDTO)).thenReturn(null);
    
    this.mockMvc.perform(put("/safebox/"+safeBoxUuid+"/items")
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMGM5ZGNjNy0xYmYzLTRjOTMtYmM5NC1lNDUwMDJlZGQ3YWIiLCJleHAiOjE2MjM4NjIzMzAs"
        + "ImlhdCI6MTYyMzg2MjE1MH0.J0HinJ2GeIrFc-8CE51sPbePkyflT4w5oaNW7qngUYlIY6NbKrmGALW1qkVPyqX7Ibt7hZgoEHLF-yskT6uy9A")
        .content(new ObjectMapper().writeValueAsBytes(putSafeBoxItemsDTO))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
