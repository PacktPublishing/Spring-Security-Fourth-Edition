package com.packtpub.springsecurity;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
public class CalendarApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_failed_Login() throws Exception {
		mockMvc.perform(post("/login")
						.accept(MediaType.TEXT_HTML)
						.contentType(
								MediaType.APPLICATION_FORM_URLENCODED)
						.param("username", "bob@example.com")
						.param("password", "bob1")
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("https://localhost:9443/cas/login?service=https%3A%2F%2Flocalhost%3A8443%2Flogin%2Fcas"))
				.andDo(print())
		;
	}


	@Test
	public void test_admin1_Login() throws Exception {
		mockMvc.perform(post("/login")
						.accept(MediaType.TEXT_HTML)
						.contentType(
								MediaType.APPLICATION_FORM_URLENCODED)
						.param("username", "admin1@example.com")
						.param("password", "admin1")
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("https://localhost:9443/cas/login?service=https%3A%2F%2Flocalhost%3A8443%2Flogin%2Fcas"))
				.andDo(print());
	}


}
