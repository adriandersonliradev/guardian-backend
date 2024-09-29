package com.guardiao.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GuardiaoIotApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardiaoIotApplication.class, args);
	}

}
