package com.progress.induction.cliqtransfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CliQTransfersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CliQTransfersApplication.class, args);
	}

}

