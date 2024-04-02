package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.configuration.SpringSecurityHints.SpringRuntimeHints;
import com.packtpub.springsecurity.service.CalendarUserDetails;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * @author bnasslahsen
 */
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(SpringRuntimeHints.class)
public class SpringSecurityHints {

	static class SpringRuntimeHints implements RuntimeHintsRegistrar {
		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			hints.reflection()
					.registerType(CalendarUserDetails.class, 
							MemberCategory.INVOKE_DECLARED_METHODS);
		}
	}
}