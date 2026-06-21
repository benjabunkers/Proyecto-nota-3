package com.duoc.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
// Inicia Eureka, donde los microservicios se registran y descubren entre si.
@SpringBootApplication
public class EurekaServerApplication {

    // Levanta el servidor de descubrimiento y su contexto Spring.
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
