package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.access.CalendarPermissionEvaluator;
import com.packtpub.springsecurity.dataaccess.EventDao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;

@Configuration
public class CustomAuthorizationConfig {

    @Bean
    public DefaultMethodSecurityExpressionHandler defaultExpressionHandler(EventDao eventDao){
        DefaultMethodSecurityExpressionHandler deh = new DefaultMethodSecurityExpressionHandler();
        deh.setPermissionEvaluator(new CalendarPermissionEvaluator(eventDao));
        return deh;
    }

	@Bean
	public DefaultHttpSecurityExpressionHandler defaultHttpSecurityExpressionHandler(){
		return new DefaultHttpSecurityExpressionHandler();
	}
}