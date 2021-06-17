package com.droar.safeish.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxItemsResponseDTO;

@DisplayName("Tests for the obtain safe box use cases service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ObtainSafeBoxItemsServiceTest {

  /** The obtain safe box items service. */
  @Autowired
  private ObtainSafeBoxItemsService obtainSafeBoxItemsService;
  
  @Test
  @DisplayName(value = "Succeeds when obtains safebox items correctly with the service within application layer")
  void givenNameAndPasswordWhenCreatingSafeBoxOnServiceThenSucceed() throws Exception {
    // Safebox and items exists previously inserted as a test case by the h2 database script
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";

    GetSafeBoxItemsResponseDTO pstSafeBoxResponse = this.obtainSafeBoxItemsService.obtainSafeBoxItems(safeBoxUuid);
    assertNotNull(pstSafeBoxResponse.getLstItemValues());
  }
}
