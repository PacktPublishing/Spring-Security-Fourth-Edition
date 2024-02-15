package com.packtpub.springsecurity.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
public class CustomAuthorizationConfig {
	
    @Description("AccessDecisionManager for Authorization voting")
	@Bean
	public AccessDecisionManager accessDecisionManager() {

		List<AccessDecisionVoter<? extends Object>> decisionVoters
				= Arrays.asList(
				new AuthenticatedVoter(), new RoleVoter(),
				new WebExpressionVoter()
		);
		return new UnanimousBased(decisionVoters);
	}
	
}