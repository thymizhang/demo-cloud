package com.demo.user.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * 引入分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        //动态表名
        //ArrayList<ISqlParser> sqlParsers = new ArrayList<>();

        //动态表名处理器
        //        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        //        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        //        tableNameHandlerMap.put("user", new ITableNameHandler() {
        //            @Override
        //            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
        //                return null;
        //            }
        //        });
        //        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);

        return new PaginationInterceptor();
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