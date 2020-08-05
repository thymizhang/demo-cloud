package com.demo.project.feign;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author thymi
 * @Date 2020/5/25
 */
@Component
@Slf4j
public class CompanyFeignFallbackFactory implements FallbackFactory<CompanyFeign> {
    @Override
    public CompanyFeign create(Throwable cause) {
        log.info(cause.getMessage());
        return new CompanyFeign() {
            @Override
            public String getCompany() {
                return defaultFallback();
            }

            @Override
            public String getCompanyUser() {
                return defaultFallback();
            }
        };
    }

    private String defaultFallback(){
        return "company服务忙, 请稍后再访问。";
    }
}
