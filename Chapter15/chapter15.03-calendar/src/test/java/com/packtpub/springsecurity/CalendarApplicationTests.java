package com.packtpub.springsecurity;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@AutoConfigureWebTestClient(timeout = "3600000")
@SpringBootTest(webEnvironment = MOCK)
public class CalendarApplicationTests {

	@Autowired
	protected WebTestClient webTestClient;

	@Test
	public void test_user1_Login() throws Exception {
		webTestClient
				.mutateWith(csrf())
				.post().uri("/login")
				.accept(MediaType.TEXT_HTML)
				.contentType(
						MediaType.APPLICATION_FORM_URLENCODED)
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectBody().returnResult();
	}


}
