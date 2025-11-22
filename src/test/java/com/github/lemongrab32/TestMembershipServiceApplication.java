package com.github.lemongrab32;

import org.springframework.boot.SpringApplication;

public class TestMembershipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MembershipServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
