package com.chrisp1985.messaging;

import org.springframework.boot.SpringApplication;

public class TestMessagingApplication {

	public static void main(String[] args) {
		SpringApplication.from(MessagingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
