package com.droar.safeish.application.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.droar.safeish.infrastructure.rest.dto.PutSafeBoxItemsDTO;

@DisplayName("Tests for the update safe box use cases service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UpdateSafeBoxItemsServiceTest {

  /** The update safe box service. */
  @Autowired
  private UpdateSafeBoxItemsService updateSafeBoxItemsService;

  @Test
  @DisplayName(value = "Succeeds when updates safebox items correctly with the service within application layer")
  @WithMockUser(username = "safeish", roles={"ADMIN"})
  void givenNameAndPasswordWhenCreatingSafeBoxOnServiceThenSucceed() throws Exception {
    // Safebox exists previously inserted as a test case by the h2 database script
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";
    PutSafeBoxItemsDTO putSafeBoxItemsDTO = new PutSafeBoxItemsDTO();
    
    List<String> lstItems = Arrays.asList("Item 1 for testing purposes", "Item 2 for testing purposes", "Item 3 for testing purposes");
    putSafeBoxItemsDTO.setLstItemsToAdd(lstItems);

    Boolean putSafeBoxResponse = this.updateSafeBoxItemsService.updateSafeBoxItems(safeBoxUuid, putSafeBoxItemsDTO);
    
    assertTrue(putSafeBoxResponse);
  }

}
