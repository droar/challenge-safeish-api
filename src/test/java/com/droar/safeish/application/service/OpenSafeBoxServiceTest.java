package com.droar.safeish.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.droar.safeish.infrastructure.rest.dto.GetSafeBoxOpenResponseDTO;

@DisplayName("Tests for the open safe box use cases service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class OpenSafeBoxServiceTest {

  /** The open safe box service. */
  @Autowired
  private OpenSafeBoxService openSafeBoxService;

  @Test
  @DisplayName(value = "Succeeds when opens a safebox correctly with the service within application layer")
  @WithMockUser(username = "safeish", roles={"ADMIN"})
  void givenNameAndPasswordWhenCreatingSafeBoxOnServiceThenSucceed() throws Exception {
    // Safebox exists previously inserted as a test case by the h2 database script
    String safeBoxUuid = "c0c9dcc7-1bf3-4c93-bc94-e45002edd7ab";
    String safeBoxPassword = "S3cur3Passw0rd##";

    GetSafeBoxOpenResponseDTO pstSafeBoxResponse = this.openSafeBoxService.openSafeBox(safeBoxUuid, safeBoxPassword);
    assertNotNull(pstSafeBoxResponse.getToken());
  }

}
