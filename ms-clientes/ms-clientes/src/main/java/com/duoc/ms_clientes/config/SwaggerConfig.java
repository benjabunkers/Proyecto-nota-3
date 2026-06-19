package com.duoc.ms_clientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Clientes")
                        .version("1.0")
                        .description("Documento de la API para la gestion de clientes del sistema de arriendo de vehiculos")
                );
    }

}
