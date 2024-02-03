package com.packtpub.springsecurity.configuration;

import javax.sql.DataSource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Spring Security Config Class
 */
@Configuration
public class AclConfig
{

    private final DataSource dataSource;

	public AclConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler(AclPermissionEvaluator permissionEvaluator,
																	AclPermissionCacheOptimizer permissionCacheOptimizer){
        DefaultMethodSecurityExpressionHandler dmseh = new DefaultMethodSecurityExpressionHandler();
        dmseh.setPermissionEvaluator(permissionEvaluator);
        dmseh.setPermissionCacheOptimizer(permissionCacheOptimizer);
        return dmseh;
    }

    @Bean
    public AclPermissionCacheOptimizer permissionCacheOptimizer(MutableAclService aclService){
        return new AclPermissionCacheOptimizer(aclService);
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService){
        return new AclPermissionEvaluator(aclService);
    }

    @Bean
    public MutableAclService aclService(LookupStrategy lookupStrategy, AclCache aclCache){
        return new JdbcMutableAclService(dataSource,
				lookupStrategy,
				aclCache);
    }

    @Bean
    public LookupStrategy lookupStrategy(AclCache aclCache,
			AclAuthorizationStrategy aclAuthorizationStrategy, ConsoleAuditLogger consoleAuditLogger){
        return new BasicLookupStrategy(
                dataSource,
				aclCache,
				aclAuthorizationStrategy,
				consoleAuditLogger);
    }

    @Bean
    public ConsoleAuditLogger consoleAuditLogger(){
        return new ConsoleAuditLogger();
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")
        );
    }


    //--- Spring Cache Configuration ---------------------------------------------//

	@Bean
	public AclCache aclCache( Cache concurrentMapCache,
			PermissionGrantingStrategy permissionGrantingStrategy, AclAuthorizationStrategy aclAuthorizationStrategy){
		return new SpringCacheBasedAclCache(concurrentMapCache,
				permissionGrantingStrategy,
				aclAuthorizationStrategy);
	}
	
	@Bean
	public PermissionGrantingStrategy permissionGrantingStrategy(){
		return new DefaultPermissionGrantingStrategy(consoleAuditLogger());
	}

	
    @Bean
    public ConcurrentMapCache concurrentMapCache(){
		return new ConcurrentMapCache("aclCache");
    }

    @Bean
    public CacheManager cacheManager(){
       return new ConcurrentMapCacheManager();
    }
	
}
