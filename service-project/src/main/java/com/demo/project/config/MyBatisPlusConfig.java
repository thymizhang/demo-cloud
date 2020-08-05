package com.demo.project.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 *
 * @Author thymi
 * @Date 2020/7/2
 */
@Configuration
@MapperScan("com.demo.project.mapper")
public class MyBatisPlusConfig {

    /**
     * 引入分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
