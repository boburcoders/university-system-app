package com.company.student.app.config.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerDocs {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Student System App")
                        .version("1.0")
                        .description("Spring Boot Student System App")
                        .termsOfService("https://spring.io/terms")
                        .contact(new Contact()
                                .name("Bobur")
                                .email("boburtoshniyozov4@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Student System App API Documentation"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        return List.of(
                GroupedOpenApi.builder()
                        .group("super-admin")
                        .pathsToMatch("/api/super-admin/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("university-admin")
                        .pathsToMatch("/api/univer-admin/**")
                        .build(),

                GroupedOpenApi.builder()
                        .group("student-profile")
                        .pathsToMatch("/api/student-profile/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("teacher-profile")
                        .pathsToMatch("/api/teacher-profile/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("auth")
                        .pathsToMatch("/api/auth/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group("file")
                        .pathsToMatch("/api/file/**")
                        .build()
        );
    }
}
