package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.repository.RememberMeTokenRepository;
import com.packtpub.springsecurity.web.authentication.rememberme.JpaTokenRepositoryCleaner;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
@author bnasslahsen
 * @since chapter 01.00
 */
@Configuration
@EnableScheduling
public class JavaConfig {

    private RememberMeTokenRepository rememberMeTokenRepository;

	public JavaConfig(RememberMeTokenRepository rememberMeTokenRepository) {
		this.rememberMeTokenRepository = rememberMeTokenRepository;
	}

	@Scheduled(fixedRate = 600_000)
	public void tokenRepositoryCleaner(){
		Thread trct = new Thread(
				new JpaTokenRepositoryCleaner(
						rememberMeTokenRepository,
						100_000L));
		trct.start();
	}

} 
