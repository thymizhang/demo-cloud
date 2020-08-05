## 工程介绍
* 统一外部接口, 微服务通过Feign调用;
* 采用sentinel进行限流, 接口熔断降级;
* 资源服务器, 统一认证与授权;  

## 工程搭建与配置
* 服务调用Feign配置和使用
> 1 加入依赖: `spring-cloud-starter-openfeign`;  
> 2 接口添加注解`@FeignClient`,注解的`value`是被调用微服务的`spring.application.name`;  
* 熔断降级sentinel配置和使用
> 1 加入依赖: `spring-cloud-starter-alibaba-sentinel` `sentinel-datasource-nacos`;  
> 2 配置文件bootstrap中增加: `sentinel.transport` `sentinel.datasource` `feign.sentinel.enabled`;  
> 3 Controller类的方法上使用注解: `@SentinelResource(value = "/xxx",blockHandler = "xxxx")`;  
* OAuth2资源服务器配置
> 1 加入依赖`spring-cloud-starter-oauth2`;  
> 2 新建配置类`ResourceServerConfiguration`,添加注解`@EnableResourceServer`,重写`configure(HttpSecurity http)`方法;  

## 问题
* 这里没有使用数据源,为何要配置数据源
> 由于common工程中依赖了mybatis-plus, 所以所有依赖common的工程都需要配置数据源;  
> common工程依赖mybatis-plus, 是因为将model都放在了common工程中, 里面有mybatis-plus的注解;  
