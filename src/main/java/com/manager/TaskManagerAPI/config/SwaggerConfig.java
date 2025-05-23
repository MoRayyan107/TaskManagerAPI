package com.manager.TaskManagerAPI.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .group("task-api")
                .displayName("Task API")
                .pathsToMatch("/api/tasks/**", "/api/auth/**")
                .build();
    }

}