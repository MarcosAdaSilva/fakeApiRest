package com.marcosDev.fake_api_us;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class FakeApiUsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FakeApiUsApplication.class, args);
	}
}
