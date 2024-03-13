package ru.vk.intern.jsonplaceholder.config;

import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Decoder feignDecoder() {
        return new GsonDecoder();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            private final ErrorDecoder defaultDecoder = new Default();
            @Override
            public Exception decode(String s, Response response) {
                return switch (response.status()) {
                    case 400 -> new RuntimeException("Bad Request");
                    case 404 -> new RuntimeException("Not found");
                    default -> defaultDecoder.decode(s, response);
                };
            }
        };
    }
}