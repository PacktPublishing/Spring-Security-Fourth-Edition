package org.apereo.cas.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;

@Lazy(false)
@AutoConfiguration
public class CasOverlayOverrideConfiguration {

	private final BaseLdapPathContextSource contextSource;

	public CasOverlayOverrideConfiguration(BaseLdapPathContextSource contextSource) {
		this.contextSource = contextSource;
	}

}
