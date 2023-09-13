package com.packtpub.springsecurity.web.configuration;

import java.util.HashSet;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Thymeleaf config.
 */
@Configuration
public class ThymeleafConfig {

	/**
	 * Template resolver spring resource template resolver.
	 *
	 * @return the spring resource template resolver
	 */
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		//resolver.setTemplateMode("HTML");
		resolver.setCacheable(false);
		resolver.setOrder(1);
		return resolver;
	}

	/**
	 * Template engine spring template engine.
	 *
	 * @param templateResolver the template resolver
	 * @return the spring template engine
	 */
	@Bean
	public SpringTemplateEngine templateEngine(final ITemplateResolver templateResolver) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);
		engine.setAdditionalDialects(new HashSet<>() {{
			add(new LayoutDialect());
		}});
		return engine;
	}

	/**
	 * Thymeleaf view resolver thymeleaf view resolver.
	 *
	 * @param templateEngine the template engine
	 * @return the thymeleaf view resolver
	 */
	@Bean
	public ThymeleafViewResolver thymeleafViewResolver(final SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine);
		return resolver;
	}

}