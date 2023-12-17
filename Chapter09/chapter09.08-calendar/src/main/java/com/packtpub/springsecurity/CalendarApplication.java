package com.packtpub.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalendarApplication {


	public static void main(String[] args) {
		//System.setProperty("javax.net.debug", "ssl:handshake");
		SpringApplication.run(CalendarApplication.class, args);
	}
}
