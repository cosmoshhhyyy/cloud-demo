package com.itcast.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.BASIC;
    }
}
