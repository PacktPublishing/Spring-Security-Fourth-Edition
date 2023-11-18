package com.packtpub.springsecurity.web.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * <p>
 * Here we leverage Spring's {@link EnableWebMvc} support. This allows more powerful configuration but still be
 * concise about it.
 * Note that this class is loaded via the WebAppInitializer
 * </p>
 *
 * @author bnasslahsen
 * @author bassLahsen
 */
@Configuration
@EnableWebMvc
@Import({ ThymeleafConfig.class })
@ComponentScan(basePackages = {
		"com.packtpub.springsecurity.web.controllers",
		"com.packtpub.springsecurity.web.model"
})
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * The Thymeleaf view resolver.
	 */
	private ThymeleafViewResolver thymeleafViewResolver;

	/**
	 * Instantiates a new Web mvc config.
	 *
	 * @param thymeleafViewResolver the thymeleaf view resolver
	 */
	public WebMvcConfig(ThymeleafViewResolver thymeleafViewResolver) {
		this.thymeleafViewResolver = thymeleafViewResolver;
	}

	/**
	 * We mention this in the book, but this helps to ensure that the intercept-url patterns prevent access to our
	 * controllers. For example, once security has been applied for administrators try commenting out the modifications
	 * to the super class and requesting <a
	 * href="http://localhost:800/calendar/events/.html">http://localhost:800/calendar/events/.html</a>. You will
	 * observe that security is bypassed since it did not match the pattern we provided. In later chapters, we discuss
	 * how to secure the service tier which helps mitigate bypassing of the URL based security too.
	 *
	 * @param registry the registry
	 */


	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/")
				.setCachePeriod(0);//Set to 0 in order to send cache headers that prevent caching

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/")
				.setCachePeriod(0)
				.resourceChain(true);
	}

	/**
	 * Configure content negotiation.
	 *
	 * @param configurer the configurer
	 */
	@Override
	public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
		configurer
				.ignoreAcceptHeader(false)
				.favorPathExtension(true) // .html / .json / .ms
				.defaultContentType(MediaType.TEXT_HTML) // text/html
				.mediaTypes(
						new HashMap<String, MediaType>() {
							{
								put("html", MediaType.TEXT_HTML);
								put("xml", MediaType.APPLICATION_XML);
								put("json", MediaType.APPLICATION_JSON);
							}
						});
	}


	/**
	 * Jackson view mapping jackson 2 json view.
	 *
	 * @return the mapping jackson 2 json view
	 */
	@Bean
	public MappingJackson2JsonView jacksonView() {
		MappingJackson2JsonView jacksonView = new MappingJackson2JsonView();
		jacksonView.setExtractValueFromSingleKeyModel(true);

		Set<String> modelKeys = new HashSet<String>();
		modelKeys.add("events");
		modelKeys.add("event");
		jacksonView.setModelKeys(modelKeys);

		return jacksonView;
	}

	/**
	 * Configure view resolvers.
	 *
	 * @param registry the registry
	 */
	@Override
	public void configureViewResolvers(final ViewResolverRegistry registry) {
		registry.viewResolver(thymeleafViewResolver);
	}


	/**
	 * Message source reloadable resource bundle message source.
	 *
	 * @return the reloadable resource bundle message source
	 */
// i18N support
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasenames("/WEB-INF/locales/messages");
		resource.setDefaultEncoding("UTF-8");
		resource.setFallbackToSystemLocale(Boolean.TRUE);
		return resource;
	}

	/**
	 * Configure default servlet handling.
	 *
	 * @param configurer the configurer
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
