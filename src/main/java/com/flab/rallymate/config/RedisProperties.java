package com.flab.rallymate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Validated
@ConfigurationProperties(prefix = "spring.redis.token")
public class RedisProperties {

    @NotBlank
    private final String host;

    @NotBlank
    private final String port;

    @ConstructorBinding
    public RedisProperties(String host, String port) {
        this.host = host;
        this.port = port;
    }
}
