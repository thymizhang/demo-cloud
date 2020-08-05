## common工程应该包括什么内容
* 工具类util
* 公共类: 比如统一返回对象dao
* 微服务接口service
* 领域模型domain (很大争议, 每个领域模型应该属于对应的微服务, 放在common完全是因为微服务接口需要用到这些domain, 导致common非常臃肿, 但是可以提高开发效率)