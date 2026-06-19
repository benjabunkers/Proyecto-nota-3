package com.duoc.api_gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void registraLasRutasDeServiciosYDocumentacion() {
        Set<String> routeIds = routeLocator.getRoutes()
                .map(route -> route.getId())
                .collectList()
                .block()
                .stream()
                .collect(Collectors.toSet());

        assertThat(routeIds).containsExactlyInAnyOrder(
                "ms-clientes",
                "ms-vehiculos",
                "ms-reservas",
                "docs-clientes",
                "docs-vehiculos",
                "docs-reservas");
    }
}
