package com.plog.realtime.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("plog backend-realtime API")
                .description("Specification")
                .version("1.0.0");
    }

    @Bean
    public GroupedOpenApi notificationApi() {
        return GroupedOpenApi.builder().group("notification").pathsToMatch("/notification/**").build();
    }
}

