package com.flab.rallymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = {"com.flab.rallymate.common.config",
	"com.flab.rallymate.domain.oauth.config"})
@SpringBootApplication
public class RallymateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RallymateApplication.class, args);
	}

}
