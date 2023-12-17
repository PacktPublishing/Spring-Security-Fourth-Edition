package com.packtpub.springsecurity.core.authority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Role;
import com.packtpub.springsecurity.repository.CalendarUserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class CalendarUserAuthoritiesMapper implements GrantedAuthoritiesMapper {

	private CalendarUserRepository userRepository;

	public CalendarUserAuthoritiesMapper(CalendarUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		authorities.forEach(authority -> {
			String email = null;
			if (authority instanceof OidcUserAuthority oidcUserAuthority) {
				OidcIdToken idToken = oidcUserAuthority.getIdToken();
				mappedAuthorities.add(oidcUserAuthority);
				email = idToken.getEmail();
			}
			else if (OAuth2UserAuthority.class.isInstance(authority)) {
				OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
				mappedAuthorities.add(oauth2UserAuthority);
				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
				email = (String) userAttributes.get("email");
			}
			if (email != null) {
				CalendarUser calendarUser = userRepository.findByEmail(email);
				List<String> roles = calendarUser.getRoles().stream().map(Role::getName).toList();
				List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
				mappedAuthorities.addAll(grantedAuthorityList);
			}
		});

		return mappedAuthorities;
	}
}