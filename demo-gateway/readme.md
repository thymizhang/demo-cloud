## 工程要点
* spring-cloud-gateway应用
* 基于gateway的sentinel限流
* 基于gateway的OAuth2认证授权

## 工程启动参数配置
VM Options : `-Dcsp.sentinel.web.servlet.block.page=https://www.baidu.com`

## spring-cloud-gateway应用
* uri地址映射,类似nginx的反向代理
* 将拦截器用于埋点控制

## 基于gateway的sentinel限流策略
在nacos中配置限流策略,对user_route路由,配置以客户端IP作为限流因子
````
[
    {
        "resource": "user_route",
        "count": 5,
        "paramItem":  {
		    "parseStrategy": 0
	    }
    }
]
````
* `parseStrategy`限流因子说明:  
以客户端IP作为限流因子  
public static final int PARAM_PARSE_STRATEGY_CLIENT_IP = 0;  
以客户端HOST作为限流因子  
public static final int PARAM_PARSE_STRATEGY_HOST = 1;  
以客户端HEADER参数作为限流因子  
public static final int PARAM_PARSE_STRATEGY_HEADER = 2;  
以客户端请求参数作为限流因子  
public static final int PARAM_PARSE_STRATEGY_URL_PARAM = 3;  
以客户端请求Cookie作为限流因子  
public static final int PARAM_PARSE_STRATEGY_COOKIE = 4;  
