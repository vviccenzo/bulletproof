package com.api.bulletproof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BulletproofApplication {

	static void main(String[] args) {
		SpringApplication.run(BulletproofApplication.class, args);
	}

}
