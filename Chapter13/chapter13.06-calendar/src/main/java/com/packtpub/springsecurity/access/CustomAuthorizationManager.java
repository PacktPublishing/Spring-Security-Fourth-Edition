package com.packtpub.springsecurity.access;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

import com.packtpub.springsecurity.intercept.RequestConfigMapping;
import com.packtpub.springsecurity.intercept.RequestConfigMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.ExpressionAuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final SecurityExpressionHandler<RequestAuthorizationContext> expressionHandler;

	private final RequestConfigMappingService requestConfigMappingService;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthorizationManager.class);
	
	public CustomAuthorizationManager(DefaultHttpSecurityExpressionHandler expressionHandler, RequestConfigMappingService requestConfigMappingService) {
		this.expressionHandler = expressionHandler;
		this.requestConfigMappingService = requestConfigMappingService;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		List<RequestConfigMapping> requestConfigMappings = requestConfigMappingService.getRequestConfigMappings();
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>(requestConfigMappings.size());

		for (RequestConfigMapping requestConfigMapping : requestConfigMappings) {
			RequestMatcher matcher = requestConfigMapping.getMatcher();

			if (matcher.matches(context.getRequest())) {
				requestMap.put(matcher, requestConfigMapping.getAttributes());

				String expressionStr = requestConfigMapping.getAttributes().iterator().next().getAttribute();
				Expression expression = this.expressionHandler.getExpressionParser().parseExpression(expressionStr);

				try {
					EvaluationContext evaluationContext = this.expressionHandler.createEvaluationContext(authentication, context);
					boolean granted = ExpressionUtils.evaluateAsBoolean(expression, evaluationContext);
					return new ExpressionAuthorizationDecision(granted, expression);
				} catch (AccessDeniedException ex) {
					logger.error("Access denied exception: {}", ex.getMessage());
					return new AuthorizationDecision(false);
				}
			}
		}

		return new AuthorizationDecision(false);
	}

}