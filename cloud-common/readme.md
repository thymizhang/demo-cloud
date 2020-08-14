## common工程应该包括什么内容
* 工具类util
* 公共类: 比如统一返回对象dao
* 微服务接口service
* 领域模型domain (很大争议, 每个领域模型应该属于对应的微服务, 放在common完全是因为微服务接口需要用到这些domain, 导致common非常臃肿, 但是可以提高开发效率)  

## 依赖说明
* 由于model使用了mybatis-plus, 导致所有依赖common的工程都需要配置数据源, 牺牲配置的冗余来提高开发效率  