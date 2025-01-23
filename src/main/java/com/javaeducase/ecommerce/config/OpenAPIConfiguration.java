package com.javaeducase.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Fedyanin Victor");
        myContact.setEmail("fedyanin.v.v@yandex.ru");

        Components authorization = new Components()
                .addSecuritySchemes("basicAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));


        Info information = new Info()
                .title("E-commerce Marketplace System API")
                .version("1.0")
                .description("""
                            This API provides endpoints \
                        for managing and utilizing a comprehensive e-commerce marketplace platform.
                            It facilitates integration for the following functionalities:
                            - User registration, authentication, and role management
                            - Product catalog management, including categories, products, offers and attributes
                            - Shopping cart operations, including adding, removing, and viewing items
                            - Order placement, tracking, and management
                            - Delivery and Payment processing
                            - Administrative features, such as user, catalog, users cart and order management
                            - Support for data validation (DaData integration) and error handling across endpoints.""")
                .contact(myContact);
        return new OpenAPI()
                .info(information)
                .components(authorization)
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .servers(List.of(server));
    }
}
