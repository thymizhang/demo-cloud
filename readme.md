## 项目概况
demo-cloud是一个实验性项目（非脚手架），基于Spring Cloud框架搭建，对主流框架、中间件、工具类进行测试，测试的重点在于：
1. 框架：版本融合，找到不同框架间最佳的兼容版本；
2. 数据库：基于RDB的分布式，快速分库分表开发；
3. 安全：基于gateway的统一入口的令牌访问，请求认证授权；
4. 可靠性：基于gateway的限流、熔断、降级策略；
5. 性能：复杂查询下的QPS极限；
项目的最终目的会形成一个基于kubernetes部署的快速开发脚手架项目（scaffold-k8s）。


## 项目介绍
1. cloud-common：公共项目，包括所有工程会用到的工具类、数据Model、DAO、所有微服务接口；
2. cloud-auth：认证授权，基于Spring Security框架实现的用户认证、token授权，支持Oauth2协议，用户认证通过service-user；
3. demo-api： 统一外部接口，包括：认证资源服务器（统一鉴权），基于sentinel的限流、熔断、降级，基于nacos服务发现，基于Feign调用下级服务；
4. demo-gateway：网关，准备取代demo-api，包括：认证资源服务器（统一鉴权），基于sentinel的限流、熔断、降级，不需要重写外部接口；
5. service-company：公司服务，包含若干用户和项目，包括：认证资源服务器（项目鉴权），基于nacos服务注册与发现，基于nacos配置抽离，基于sentinel熔断回调，基于feign服务间调用，mybatis-plus逆向工程；
6. service-project：项目服务，包括：基于nacos服务注册与发现，基于nacos配置抽离，基于feign服务间调用，JDK8特性测试，mybatis-plus数据映射、分页；
7. service-user：用户服务，包括：基于nacos服务注册与发现，基于nacos配置抽离，基于feign服务间调用，基于sharding-jdbc读写分离，hystrix服务熔断降级，redis缓存；


## 项目运行环境
1. JDK 11.0.8
2. MySQL 8.0.21
3. Redis 6.0.8
4. RabbitMQ 3.8.9
5. Nacos 1.4.0
6. Sentinel 1.8.0


## 不同环境启动说明
* 命令行启动(windows环境下需要指定编码)  
```
java -Dport=8080 -Dfile.encoding=UTF-8 -jar service-project-1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

## SpringCloud的架构缺陷
> 1 由于采用Http协议通信, 服务由Controller实现接口, 服务间的互相访问和外部访问服务都通过这个接口, 由于鉴权通常在Controller层通过注解实现, 可能导致服务之间调用时会受鉴权影响;  
> 2 如果服务间的调用通过其他方式(比如:rpc), 需要再配置一套服务端口及服务发现规则, 导致代码维护工作增大;  
> 本工程微服务的Controller后期改名为ServiceImpl, 对外开放接口单独建立Web工程, 由Web工程的Controller对外开放接口, 鉴权也统一在Controller完成.  

## 问题
* maven自动下载依赖问题(工程JDK版本为11)  
> 在Setting->Build,Execution,Deployment->Maven->Importing->设置JDK为1.8

* 服务启动后一直报错误: com.alibaba.nacos.client.naming: [NA] failed to request 
> SpringBoot SpringCloud SpringCloudAlibaba三者版本不对所致,以下时亲测有效版本:  
> SpringBoot:2.3.2.RELEASE  
> SpringCloud:Hoxton.SR6  
> SpringCloudAlibaba:2.2.1.RELEASE
> 说明一下: SpringCloudAlibaba官方支持的最高SpringCloud版本是Greenwich.SR5, Greenwich对应的SpringBoot版本是2.1.x, 这里做了跨版本尝试  
> 版本对照地址: https://spring.io/projects/spring-cloud , 详见Table 1. Release train Spring Boot compatibility

* 服务调用者重启后,第一次调用远程服务,报:failed to write cache for dom  
> 原因: 消费者使用Feign声明式服务调用方式，配合Ribbon实现客户端负载均衡，使用Hystrix来解决熔断问题，防止服务阻塞，但是消费者在启动后，第一次调用服务提供者的服务时，出现上述异常；本质原因是Ribbon的负载均衡相关初始化工作没有完成.  
> 方案一: 延长Hystrix熔断器的超时时间,该配置默认是1秒，1秒在首次调用时，初始化Ribbon相关信息是不够的，所以每次首次调用都会报错  
> hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=10000  
> 方案二: Ribbon选用饥饿加载模式eager-load  
> ribbon.eager-load.enabled=true  
> ribbon.eager-load.clients=phoenix-provider  

* 如果需要nacos来管理sentinel的流控和降级规则,需要在应用启动后再重启sentinel-dashboard
> sentinel只能找到public命名空间下的动态数据源配置;  
> 如果在应用中新指定了nacos的流控配置, 需要重启sentinel-dashboard才能生效;  
> 如果使用nacos管理sentinel规则数据源, 每种规则(限流、降级)只能指定一个文件;  
> 如果使用nacos管理sentinel动态数据源, 需要引入`sentinel-datasource-nacos`依赖;  

* SpringBoot测试类无法自动注入@Autowired
> 加入以下注解
> @RunWith(SpringRunner.class)  
> @SpringBootTest(classes = Application.class) #启动类  
> 重点1: 测试类的命名要遵循规则: Application类名 + "Test"
> 重点2: 启动类是否添加注解@MapperScan("xxx.xxx.xxx")

* MyBatisPlus的service接口调用出现`can not find lambda cache for this entity`
> 经测试,使用普通表达式`QueryWrapper`可以实现查询的  
> 使用`LambdaQueryWrapper`会报以上错误  
> `LambdaQueryWrapper`在mapper中是可以使用的  

* 报javax.xml.bind.JAXBException异常(好像是sharding-jdbc有用到这个包)  
> 原因 : JAXB API是java EE 的API，因此在java SE 9.0 中不再包含这个 Jar 包  
> 解决方案1 : 将jdk降级到1.8  
> 解决方案2 : 增加以下依赖  
```
        <dependency>
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>2.3.3</version>
        </dependency>
```

* 关于同时配置OAuth2和Sharding-jdbc数据源后, Sharding-jdbc数据源读写分离失效的问题
> 说明:  
> 1 OAuth2配置数据库模式, 需要在yml单独配置数据源才能启动成功`@ConfigurationProperties(prefix = "spring.datasource")`;  
> 2 Sharding-jdbc由yml配置多数据源;  
>
> 结果: 系统默认采用`spring.datasource`配置的数据源, 导致Sharding-jdbc失效.  
>
> 解决思路: 
> 1 将认证授权服务与用户服务剥离, 认证授权服务独立使用数据源, 剥离的用户服务采用sharding-jdbc数据源;  
> 2 认证授权服务使用用户登录服务时, 采用远程调用(feign)的方式.  


* 打包问题: 依赖common的工程打包提示:程序包XXXX不存在  
> 原因: springboot工程默认打包是可执行包(包含BOOT-INF目录), 需要将默认包打成非可执行包  
> 解决: 在pom中增加打包参数classifier, 将可执行包与非可执行包分开打包  
```
<plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
          <!-- 指定classifier, 打包时会将可执行包与非可执行包分开打包, 避免依赖工程找不到依赖 -->
          <classifier>exec</classifier>
      </configuration>
  </plugin>
```