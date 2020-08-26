package com.demo.auth;

import com.demo.common.util.JvmUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author thymi
 * @Date 2020/6/1
 */
@SpringCloudApplication
@EnableFeignClients
@EnableConfigurationProperties
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
        JvmUtil.getMemoryStatus();
    }
}
