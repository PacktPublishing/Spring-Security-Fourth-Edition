package com.packtpub.springsecurity.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Here we leverage Spring's {@link org.springframework.web.servlet.config.annotation.EnableWebMvc} support. This allows more powerful configuration but still be
 * concise about it.
 * Note that this class is loaded via the WebAppInitializer
 * </p>
 *
 * @since chapter 01.00
 */

/**
 * @author Rob Winch
 * @author Mick Knutson
 * @author bnasslahsen
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
