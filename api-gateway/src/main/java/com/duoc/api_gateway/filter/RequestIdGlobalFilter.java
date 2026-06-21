package com.duoc.api_gateway.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

// Agrega un identificador unico a cada peticion para rastrearla entre microservicios.
@Component
public class RequestIdGlobalFilter implements GlobalFilter, Ordered {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";

    // Reutiliza el X-Request-Id recibido o genera uno antes de continuar la cadena.
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String incomingRequestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        String requestId = incomingRequestId == null || incomingRequestId.isBlank()
                ? UUID.randomUUID().toString()
                : incomingRequestId;

        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.set(REQUEST_ID_HEADER, requestId))
                .build();
        exchange.getResponse().getHeaders().set(REQUEST_ID_HEADER, requestId);

        return chain.filter(exchange.mutate().request(request).build());
    }

    // Prioridad alta para identificar la peticion antes que otros filtros.
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
