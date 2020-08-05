package com.demo.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局拦截器, 这里可以做埋点控制
 *
 * @Author thymi
 * @Date 2020/7/17
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        log.info("请求地址: " + exchange.getRequest().getPath().toString());
        // 没有令牌直接返回401错误
        if(token == null || token.trim().isEmpty()){
            log.info("没有token, 请求终止, 返回401 ...");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        log.info("拦截到token : " + token);

        return chain.filter(exchange);
    }

    /**
     * 数值越低,加载顺序越靠前,默认值:0
     *
     * @return
     */
//    @Override
//    public int getOrder() {
//        return -100;
//    }
}
