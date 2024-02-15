package com.packtpub.springsecurity.configuration;

import java.util.Arrays;
import java.util.List;

import com.packtpub.springsecurity.access.CalendarPermissionEvaluator;
import com.packtpub.springsecurity.dataaccess.EventDao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.vote.ConsensusBased;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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
	public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler (){
		return new DefaultWebSecurityExpressionHandler();
	}
	
    @Bean
    public AccessDecisionManager accessDecisionManager(
			DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler) {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(){{
                    setExpressionHandler(defaultWebSecurityExpressionHandler);
                }}
        );
        return new ConsensusBased(decisionVoters);
    }
}