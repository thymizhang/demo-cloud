package com.demo.api;

import com.demo.common.util.JvmUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author thymi
 * @Date 2020/7/3
 */
@SpringCloudApplication
@EnableFeignClients
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        JvmUtil.getMemoryStatus();
    }
}
