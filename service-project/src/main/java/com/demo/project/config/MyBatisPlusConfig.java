package com.demo.project.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
     * mybatis plus分页插件配置，3.4.0及以上版本<br/>
     * 参考：https://mybatis.plus/guide/interceptor.html#mybatisplusinterceptor
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
