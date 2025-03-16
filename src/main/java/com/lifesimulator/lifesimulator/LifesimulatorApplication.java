package com.lifesimulator.lifesimulator;

import com.lifesimulator.lifesimulator.services.GameService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class LifesimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifesimulatorApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(GameService gameService) {
		return args -> gameService.startGame();
	}
}
