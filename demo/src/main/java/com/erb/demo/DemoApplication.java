package com.erb.demo;

import com.erb.demo.Annotation.LogExecutionTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@EnableCaching
@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);


		String rawPassword = "test123";

		String encoded = new BCryptPasswordEncoder().encode(rawPassword);
		System.out.println("Encoded password: " + encoded);
	}
}
