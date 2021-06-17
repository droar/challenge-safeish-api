package com.droar.safeish.application;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.droar.safeish.infrastructure.rest.SafeBoxController;

/**
 * Simple test to check if the context loads
 * 
 * @author droar
 *
 */
@SpringBootTest
class ContextTest {

  @Autowired
  private SafeBoxController safeBoxController;

  @Test
  void contextLoads() {
    assertThat(safeBoxController).isNotNull();
  }

}
