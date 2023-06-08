package com.flab.rallymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "com.flab.rallymate.common.config")
@SpringBootApplication
public class RallymateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RallymateApplication.class, args);
	}

}
