package com.duoc.api_gateway.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

class RequestIdGlobalFilterTest {

    private final RequestIdGlobalFilter filter = new RequestIdGlobalFilter();

    @Test
    void generaYPropagaRequestIdCuandoNoVieneEnLaSolicitud() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/v1/clientes"));
        AtomicReference<ServerWebExchange> filteredExchange = new AtomicReference<>();

        filter.filter(exchange, currentExchange -> {
            filteredExchange.set(currentExchange);
            return Mono.empty();
        }).block();

        String requestId = filteredExchange.get().getRequest().getHeaders()
                .getFirst(RequestIdGlobalFilter.REQUEST_ID_HEADER);
        assertThat(requestId).isNotBlank();
        assertThat(exchange.getResponse().getHeaders().getFirst(RequestIdGlobalFilter.REQUEST_ID_HEADER))
                .isEqualTo(requestId);
    }

    @Test
    void conservaRequestIdRecibido() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/v1/reservas")
                .header(RequestIdGlobalFilter.REQUEST_ID_HEADER, "solicitud-123"));
        AtomicReference<ServerWebExchange> filteredExchange = new AtomicReference<>();

        filter.filter(exchange, currentExchange -> {
            filteredExchange.set(currentExchange);
            return Mono.empty();
        }).block();

        assertThat(filteredExchange.get().getRequest().getHeaders()
                .getFirst(RequestIdGlobalFilter.REQUEST_ID_HEADER)).isEqualTo("solicitud-123");
        assertThat(exchange.getResponse().getHeaders().getFirst(RequestIdGlobalFilter.REQUEST_ID_HEADER))
                .isEqualTo("solicitud-123");
    }
}
