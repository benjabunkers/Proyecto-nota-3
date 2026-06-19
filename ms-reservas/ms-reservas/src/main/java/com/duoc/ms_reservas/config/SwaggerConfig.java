package com.duoc.ms_reservas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Reservas")
                        .version("1.0")
                        .description("Documentacion de la API para gestionar reservas y sus estados. "
                                + "Las reservas validan clientes y vehiculos mediante Feign con ms-clientes y ms-vehiculos."));
    }
}