package com.demo.monitor.Hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * Hystrix监控台
 *
 * @Author thymi
 * @Date 2020/5/25
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixMonitorApplication.class,args);
    }
}
