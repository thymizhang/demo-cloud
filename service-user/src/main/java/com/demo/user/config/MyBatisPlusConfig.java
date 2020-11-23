package com.demo.user.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * MyBatisPlus配置类
 *
 * @Author thymi
 * @Date 2020/6/11
 */
@Configuration
@MapperScan("com.demo.user.mapper")
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

    /**
     * 乐观锁插件, 在多读场景下使用, 不推荐
     *
     * @return
     */
    //    @Bean
    //    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
    //        return new OptimisticLockerInterceptor();
    //    }

    /**
     * 性能分析插件, 官方已经不推荐该插件, 推荐第三方扩展: p6spy
     *
     * @return
     */
    //    @Bean
    //    @Profile({"dev","test"})
    //    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
    //        return new PerformanceMonitorInterceptor();
    //    }

}