package com.packtpub.springsecurity.configuration;

import java.io.IOException;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * The type Java config.
 */
@Configuration
@Import(DataSourceConfig.class)
@ComponentScan(basePackages =
		{
				"com.packtpub.springsecurity.dataaccess",
				"com.packtpub.springsecurity.domain",
				"com.packtpub.springsecurity.service"
		}
)
public class JavaConfig {

	/**
	 * Note: If you want to use @PropertySource, you must create a static
	 * PropertySourcesPlaceholderConfigurer with the @Bean as seen here.
	 *
	 * @return PropertySourcesPlaceholderConfigurer property sources placeholder configurer
	 * @throws IOException the io exception
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(Boolean.TRUE);
		propertySourcesPlaceholderConfigurer.setProperties(yamlPropertiesFactoryBean().getObject());
		return propertySourcesPlaceholderConfigurer;
	}

	/**
	 * Yaml properties factory bean yaml properties factory bean.
	 *
	 * @return the yaml properties factory bean
	 */
	@Bean
	public static YamlPropertiesFactoryBean yamlPropertiesFactoryBean() {
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("application.yml"));
		return yaml;
	}
} // The end...
