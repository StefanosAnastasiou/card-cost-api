package com.etraveli.cardcostapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CardCostApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardCostApiApplication.class, args);
	}

}
