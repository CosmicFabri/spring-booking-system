package com.spring.spring_booking_system;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBookingSystemApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		System.setProperty("JDBC_DATABASE_URL", dotenv.get("DB_URL"));
		System.setProperty("JDBC_DATABASE_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("JDBC_DATABASE_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION_TIME", dotenv.get("JWT_EXPIRATION"));

		SpringApplication.run(SpringBookingSystemApplication.class, args);
	}

}
