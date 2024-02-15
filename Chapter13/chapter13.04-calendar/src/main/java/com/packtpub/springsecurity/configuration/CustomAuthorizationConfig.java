package com.packtpub.springsecurity.configuration;

import java.util.Arrays;
import java.util.List;

import com.packtpub.springsecurity.access.CalendarPermissionEvaluator;
import com.packtpub.springsecurity.access.expression.CustomWebSecurityExpressionHandler;
import com.packtpub.springsecurity.authentication.CalendarUserAuthenticationProvider;
import com.packtpub.springsecurity.dataaccess.EventDao;
import com.packtpub.springsecurity.service.CalendarService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.vote.ConsensusBased;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
public class CustomAuthorizationConfig {

    @Bean
    public DefaultMethodSecurityExpressionHandler defaultExpressionHandler(EventDao eventDao){
        DefaultMethodSecurityExpressionHandler deh = new DefaultMethodSecurityExpressionHandler();
        deh.setPermissionEvaluator(
                new CalendarPermissionEvaluator(eventDao));
        return deh;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager(
			CustomWebSecurityExpressionHandler customWebSecurityExpressionHandler) {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(){{
                    setExpressionHandler(customWebSecurityExpressionHandler);
                }}
        );
        return new ConsensusBased(decisionVoters);
    }

	@Bean @DependsOn({"defaultCalendarService"})
	public CalendarUserAuthenticationProvider calendarUserAuthenticationProvider(
			CalendarService calendarService,
			PasswordEncoder passwordEncoder){
		return new CalendarUserAuthenticationProvider(calendarService, passwordEncoder);
	}
}