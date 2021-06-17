package com.droar.safeish.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxDTO;
import com.droar.safeish.infrastructure.rest.dto.PostSafeBoxResponseDTO;

@DisplayName("Tests for the create safe box use cases service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CreateSafeBoxServiceTest {

  /** The create safe box service. */
  @Autowired
  private CreateSafeBoxService createSafeBoxService;

  @Test
  @DisplayName(value = "Succeeds when creates a safebox correctly with the service within application layer")
  void givenNameAndPasswordWhenCreatingSafeBoxOnServiceThenSucceed() throws Exception {

    PostSafeBoxDTO createSafeBoxMockDTO = new PostSafeBoxDTO();
    createSafeBoxMockDTO.setName("Testing creation SafeBox 1");
    createSafeBoxMockDTO.setPassword("S3cur3Passw0rd##");

    PostSafeBoxResponseDTO pstSafeBoxResponse = this.createSafeBoxService.createSafeBox(createSafeBoxMockDTO);
    
    assertNotNull(pstSafeBoxResponse.getUuid());
  }

}
