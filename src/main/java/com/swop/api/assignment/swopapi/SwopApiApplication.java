package com.swop.api.assignment.swopapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Exchange Rate APIs", version = "1.0",
		description = "Documentation Exchange Rate APIs v1.0")
)
public class SwopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwopApiApplication.class, args);
	}

}
