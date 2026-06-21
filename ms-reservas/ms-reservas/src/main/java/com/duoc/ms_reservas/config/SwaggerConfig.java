package com.duoc.ms_reservas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configura los metadatos que Swagger UI muestra para este microservicio.
// Las anotaciones de los controladores describen cada endpoint y sus respuestas.
@Configuration
public class SwaggerConfig {

    // Crea la definicion OpenAPI con el nombre, version y descripcion de la API.
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