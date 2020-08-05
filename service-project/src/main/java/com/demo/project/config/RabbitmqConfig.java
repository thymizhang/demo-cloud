package com.demo.project.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author thymi
 * @Date 2020/5/11
 */
//@Configuration
//@Data
//@RefreshScope
public class RabbitmqConfig {
    /**
     * MQ地址
     */
    @Value("${spring.rabbitmq.addresses}")
    String addresses;

    /**
     * MQ登录名
     */
    @Value("${spring.rabbitmq.username}")
    String username;

    /**
     * MQ登录密码
     */
    @Value("${spring.rabbitmq.password}")
    String password;

    /**
     * MQ的虚拟主机名
     */
    @Value("${spring.rabbitmq.virtual-host}")
    String vHost;

}
