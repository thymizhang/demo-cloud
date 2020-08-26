package com.demo.project;

import com.demo.common.util.JvmUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目模块启动类
 * <p>
 * 注解: @SpringCloudApplication包括了@SpringBootApplication、@EnableDiscoveryClient、@EnableCircuitBreaker三个注解
 *
 * @Author thymi
 * @Date 2020/5/7
 */
//@SpringBootApplication
//@EnableDiscoveryClient
//@EnableCircuitBreaker
@SpringCloudApplication
@EnableFeignClients
public class ProjectServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceApplication.class, args);
        JvmUtil.getMemoryStatus();
    }
}
