package com.droar.safeish;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Activation of spring xml spring beans.
 * 
 * @author droar
 *
 */
@Configuration
@ImportResource({"classpath:/spring/spring-beans.xml",})
public class AppConfiguration {}
