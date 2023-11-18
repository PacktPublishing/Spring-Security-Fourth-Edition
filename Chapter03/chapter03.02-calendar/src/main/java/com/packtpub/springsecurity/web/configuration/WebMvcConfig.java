package com.packtpub.springsecurity.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web mvc config.
 *
 *  @author bnasslahsen
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * Add view controllers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/login/form")
				.setViewName("login");
		registry.addViewController("/errors/403")
				.setViewName("/errors/403");
		registry.addViewController("/index")
				.setViewName("index");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

}
