package com.tlavu.educore.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long start = System.currentTimeMillis();

        String method = request.getMethod().name();
        String path = request.getURI().toString();
        String remote = request.getRemoteAddress() != null ? request.getRemoteAddress().toString() : "-";

        log.info("Incoming request: {} {} from {}", method, path, remote);

        return chain.filter(exchange)
                .doOnEach(signal -> {
                    // no-op here; use doFinally to ensure logging on complete/error/cancel
                })
                .doFinally(signalType -> {
                    long duration = System.currentTimeMillis() - start;
                    Integer status = null;
                    try {
                        status = exchange.getResponse().getStatusCode() != null
                                ? exchange.getResponse().getStatusCode().value()
                                : null;
                    } catch (Exception ex) {
                        log.warn("Could not get response status code: {}", ex.getMessage());
                    }
                    if (status != null) {
                        log.info("Completed {} {} -> {} ({} ms)", method, path, status, duration);
                    } else {
                        log.info("Completed {} {} -> (unknown status) ({} ms)", method, path, duration);
                    }
                });
    }
}
