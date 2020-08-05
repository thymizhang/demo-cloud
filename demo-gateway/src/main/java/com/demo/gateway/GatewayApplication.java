package com.demo.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关配置重点:
 * 1 限流熔断: 采用sentinel,无侵入,效率高,如果是hystrix+redis,存在性能问题
 *
 * @Author thymi
 * @Date 2020/7/17
 */
@SpringCloudApplication
@Slf4j
@RestController
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping("/fallback")
    public String fallback(){
        return "您看到的是限流熔断页面";
    }

    /**
     * 如果拦截器配置了令牌桶, 这里需要注入KeyResolver接口的实现
     *
     * @return
     */
    @Bean
    public KeyResolver uriKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                // 获取到访问者的ip地址
                //log.info(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
                log.info(exchange.getRequest().getRemoteAddress().getHostName());
                return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
            }
        };
    }

    /**
     * 获取请求用户ip作为限流key
     *
     * @return
     */
    //@Bean
    public KeyResolver hostAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 获取请求用户id作为限流key
     *
     * @return
     */
    //@Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

    /**
     * 获取请求地址的uri作为限流key。
     *
     * @return
     */
    //@Bean
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }
}
