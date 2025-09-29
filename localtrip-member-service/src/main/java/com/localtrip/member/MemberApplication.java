package com.localtrip.member;

import com.localtrip.auth.config.LocalTripAuthAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.localtrip")
@Import(LocalTripAuthAutoConfiguration.class)
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

}
