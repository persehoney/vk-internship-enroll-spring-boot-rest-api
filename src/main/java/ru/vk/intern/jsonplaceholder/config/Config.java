package ru.vk.intern.jsonplaceholder.config;

import feign.codec.Decoder;
import feign.gson.GsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Decoder feignDecoder() {
        return new GsonDecoder();
    }
}