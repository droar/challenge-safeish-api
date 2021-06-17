package com.droar.safeish.commons;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Custom security handler for entry point.
 * 
 * This class will handle spring security forbidden and unauthorized attempts
 * 
 * @author droar
 *
 */
@Component
public class CustomAccessDeniedEntryPoint implements AuthenticationEntryPoint {

  /** The message resource. */
  @Autowired
  private ReloadableResourceBundleMessageSource messageSource;

  @SuppressWarnings("resource")
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(new JSONObject()
            .put("status", HttpServletResponse.SC_UNAUTHORIZED)
            .put("timestamp", LocalDateTime.now())
            .put("message", this.messageSource.getMessage("http.auth_error", null, LocaleContextHolder.getLocale()))
            .toString());
  }

}
