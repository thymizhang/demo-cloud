package com.demo.service.company.config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * MyBatis-Plus代码生成器, 从数据库反向生成domain、mapper、service
 * 需要添加依赖
 * <dependency>
 * <groupId>com.baomidou</groupId>
 * <artifactId>mybatis-plus-generator</artifactId>
 * <version>3.3.2</version>
 * </dependency>
 *
 * @Author thymi
 * @Date 2020/6/10
 */
public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 1 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        // 获取当前用户目录
        String projectPath = System.getProperty("user.dir");
        // 输出目录
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("thymi");
        globalConfig.setOpen(false);
        // 时间类指定
        globalConfig.setDateType(DateType.TIME_PACK);
        // 自动配置Swagger2
        globalConfig.setSwagger2(false);
        autoGenerator.setGlobalConfig(globalConfig);

        // 2 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://mysql.gceasy.cc:3306/gceasy_demo?useSSL=false&serverTimezone=Hongkong&useUnicode=true&characterEncoding=UTF-8");
        // dsc.setSchemaName("public");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("gceasy2020");
        autoGenerator.setDataSource(dataSourceConfig);

        // 3 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName("user");
        packageConfig.setParent("com.demo.auth")
                .setEntity("pojo")
                .setMapper("mapper")
                .setService("service")
                .setController("controller");
        autoGenerator.setPackageInfo(packageConfig);

        // 4 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 设置哪些表需要映射, 默认所有表
        strategy.setInclude("tb_user");
        // 下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 设置逻辑删除字段的名称
        strategy.setLogicDeleteFieldName("deleted");
        // 设置自动填充
        //strategy.setTableFillList();
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        autoGenerator.setStrategy(strategy);

        autoGenerator.execute();

    }

}
