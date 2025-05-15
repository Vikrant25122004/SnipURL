package com.SnipURL.SnipURL.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.awt.SystemColor.info;

@Configuration
public class Swagger_Config {
    @Bean
    public OpenAPI mycustomconfig(){
        return new OpenAPI().info(new Info().title("SnipURL Documentation"));
    }
}
