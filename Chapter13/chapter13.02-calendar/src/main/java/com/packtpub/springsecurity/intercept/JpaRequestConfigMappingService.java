package com.packtpub.springsecurity.intercept;

/**
 * @author bnasslahsen
 */

import java.util.Comparator;
import java.util.List;

import com.packtpub.springsecurity.domain.SecurityFilterMetadata;
import com.packtpub.springsecurity.repository.SecurityFilterMetadataRepository;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Repository;

@Repository
public class JpaRequestConfigMappingService implements RequestConfigMappingService {

	private final SecurityFilterMetadataRepository securityFilterMetadataRepository;

	public JpaRequestConfigMappingService(final SecurityFilterMetadataRepository securityFilterMetadataRepository) {
		this.securityFilterMetadataRepository = securityFilterMetadataRepository;
	}
	
	
    //.requestMatchers("/webjars/**").permitAll()
	//.requestMatchers("/css/**").permitAll()
	//.requestMatchers("/favicon.ico").permitAll()
	//.requestMatchers("/admin/h2/**")
	//.access(new WebExpressionAuthorizationManager("isFullyAuthenticated() and hasRole('ADMIN')"))
	//			.requestMatchers("/").permitAll()
	//.requestMatchers("/login/*").permitAll()
	//.requestMatchers("/logout").permitAll()
	//.requestMatchers("/signup/*").permitAll()
	//.requestMatchers("/errors/**").permitAll()
	//.requestMatchers("/admin/*").hasRole("ADMIN")
	//.requestMatchers("/events/").hasRole("ADMIN")
	//.requestMatchers("/**").hasRole("USER"))
	@Override
	public List<RequestConfigMapping> getRequestConfigMappings() {
		return securityFilterMetadataRepository
				.findAll()
				.stream()
				.sorted(Comparator.comparingInt(SecurityFilterMetadata::getSortOrder))
				.map(md -> new RequestConfigMapping(
						new AntPathRequestMatcher(md.getAntPattern()),
						new SecurityConfig(md.getExpression()))).toList();
	}


}