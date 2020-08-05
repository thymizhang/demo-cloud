package com.demo.project.feign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 服务熔断降级, 对服务有相应要求的配置<br/>
 * FallbackFactory必须满足三个条件
 * 1 必须实现FallbackFactory接口;
 * 2 必须返回泛型中的接口实现;
 * 3 必须是标准的spring bean;
 *
 * @Author thymi
 * @Date 2020/5/25
 */
@Component
@Slf4j
public class UserFeignFallbackFactory implements FallbackFactory<UserFeign> {

    @Override
    public UserFeign create(Throwable cause) {

        log.info(cause.getMessage());
        log.info(String.valueOf(cause.getCause()));

        return new UserFeign() {
            @Override
            public String getUser() {
                return defaultFallback();
            }

            @Override
            public String getProject() {
                return defaultFallback();
            }
        };
    }

    private String defaultFallback(){
        return "服务器忙, 请稍后再访问。";
    }
}
