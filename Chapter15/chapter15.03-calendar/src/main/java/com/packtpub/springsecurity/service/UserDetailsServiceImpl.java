package com.packtpub.springsecurity.service;

import java.util.HashSet;
import java.util.Set;

import com.packtpub.springsecurity.domain.Role;
import com.packtpub.springsecurity.repository.CalendarUserRepository;
import reactor.core.publisher.Mono;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


/**
 * @author bnasslahsen
 */
@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

	private final CalendarUserRepository userRepository;

	public UserDetailsServiceImpl(CalendarUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByEmail(username)
				.flatMap(user -> {
					Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
					for (Role role : user.getRoles()) {
						grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
					}
					return Mono.just(new User(user.getEmail(), user.getPassword(), grantedAuthorities));
				});
	}

}
