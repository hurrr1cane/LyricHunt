package com.mhorak.lyrichunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LyricHunterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LyricHunterApplication.class, args);
	}

}
