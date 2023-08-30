package com.flab.rallymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = {"com.flab.rallymate.config",
        "com.flab.rallymate.auth"})
@SpringBootApplication
public class RallymateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RallymateApplication.class, args);
    }

}
