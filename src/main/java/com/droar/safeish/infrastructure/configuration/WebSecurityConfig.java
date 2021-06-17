package com.droar.safeish.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.droar.safeish.commons.CustomAccessDeniedEntryPoint;

/**
 * Simple web security class, for handling the API security.
 * Will throw custom error messages in case of auth fail
 * 
 * @author droar
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
  /** The entry point custom handler. */
  @Autowired
  private CustomAccessDeniedEntryPoint customForbiddenEntryPoint;

  /** The jwt user details service. */
  @Autowired
  private UserDetailsService jwtUserDetailsService;

  /** The jwt request filter. */
  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }
  
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
    .csrf().disable()
    .authorizeRequests()
    .antMatchers("/safebox/*/open").permitAll()
    .antMatchers("/safebox").permitAll()
    .anyRequest().authenticated()
    .and()
    .httpBasic()
    .and()
    .exceptionHandling().authenticationEntryPoint(this.customForbiddenEntryPoint)
    .and()
    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    
    // Add a filter to validate the tokens with every request
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
    
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/h2-console/**");
  }
    
}