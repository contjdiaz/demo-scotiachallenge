package com.scotiachallenge.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.scotiachallenge.demo.infraestructure.persistence.repository"})
public class ScotiachallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScotiachallengeApplication.class, args);
	}

}
