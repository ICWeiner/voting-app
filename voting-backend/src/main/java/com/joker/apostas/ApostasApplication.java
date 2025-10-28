package com.joker.apostas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApostasApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApostasApplication.class, args);
	}

}
