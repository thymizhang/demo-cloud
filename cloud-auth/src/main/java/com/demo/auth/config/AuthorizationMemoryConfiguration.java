package com.demo.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 配置基于内存的认证服务器, 测试用<br/>
 * <strong>授权码模式:</strong><br/>
 * 第一步: 请求授权码(内存): http://localhost:8080/oauth/authorize?client_id=client&response_type=code&scope=app&redirect_uri=http://localhost:8080/auth/redirect <br/>
 * 第二步: 请求令牌(内存)/x-www-from-urlencoded: http://client:secret@localhost:8080/oauth/token 参数: grant_type、code、redirect_uri <br/>
 * 第三步: 带着令牌访问资源服务器url <br/>
 * <strong>简化模式:</strong><br/>
 * 适用场景: 没有服务器的客户端
 * 直接请求令牌: http://localhost:8080/oauth/authorize?client_id=client&response_type=token&scope=app&redirect_uri=http://localhost:8080/auth/redirect <br/>
 * 返回含令牌的url: http://localhost:8080/auth/redirect#access_token=c31b371d-6b7d-4f29-bb63-c4da932a3477&token_type=bearer&expires_in=7199 <br/>
 * <strong>密码模式:</strong>
 * 直接请求令牌/x-www-from-urlencoded: http://client:secret@localhost:8080/oauth/token 参数: client_id、client_secret、grant_type、username、password <br/>
 * <strong>客户端模式:</strong>
 * 使用场景: 一般用于内部系统
 * 直接请求令牌/x-www-from-urlencoded: http://client:secret@localhost:8080/oauth/token 参数: client_id、client_secret、grant_type <br/>
 * 客户端模式是申请不到刷新令牌的<br/>
 * <strong>刷新令牌:</strong>
 * 请求新令牌/x-www-from-urlencoded: http://client:secret@localhost:8080/oauth/token 参数: grant_type、refresh_token <br/>
 * <p>
 * <strong>使用说明</strong> <br/>
 * 1 系统使用的是基于数据库方式的认证授权, 使用内存方式需要打开这些注解: `@Configuration`、`@EnableAuthorizationServer`、`@Bean`<br/>
 * 2 在WebSecurityConfiguration将tokenStore的模式改为InMemoryTokenStore或JwtTokenStore.<br/>
 * <br/>
 *
 * @Author thymi
 * @Date 2020/7/9
 */
//@Configuration
//@EnableAuthorizationServer
public class AuthorizationMemoryConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 加密工具, 在WebSecurityConfiguration注入
     */
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 认证管理器, 在WebSecurityConfiguration注入
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * 用户详情服务, 在WebSecurityConfiguration注入
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 令牌存储策略, 在WebSecurityConfiguration注入
     */
    @Autowired
    TokenStore tokenStore;

    /**
     * JWT令牌转换器, 在WebSecurityConfiguration注入
     */
    @Autowired
    JwtAccessTokenConverter accessTokenConverter;

    /**
     * 用来配置客户端详情服务(ClientDetailsService),客户端详情信息在这里初始化
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 通过内存获取客户端信息
        // inMemory : 内存配置客户端信息
        // withClient : 客户端标识id
        // secret : 客户端安全码,要进行bcrypt加密
        // resourceIds : 客户端可以访问的资源服务id
        // authorizedGrantTypes : 客户端允许的授权类型(共5种), 可以全部允许
        // scopes : 客户端授权范围, 每个授权范围会独立分配授权码
        // autoApprove : false 跳转到授权页面, true 不跳转直接发放令牌. (仅适用用于authorization_code授权类型)
        // redirectUris : 授权码回调地址
        clients.inMemory()
                .withClient("client")
                .secret(bCryptPasswordEncoder.encode("secret"))
                .resourceIds("demo-api", "service-project")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                .scopes("app", "web")
                .autoApprove(false)
                .accessTokenValiditySeconds(180)
                .refreshTokenValiditySeconds(259200)
                .redirectUris("http://localhost:8080/auth/redirect");
    }

    /**
     * 配置令牌的访问端点和令牌服务,其核心就是tokenStore和tokenServices.<br/>
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.authorizationCodeServices(new InMemoryAuthorizationCodeServices())
                // AuthenticationManager必须配置, 否则会导致password模式无效
                .authenticationManager(authenticationManager)
                //.tokenServices(authorizationServerTokenServices())
                //.allowedTokenEndpointRequestMethods(HttpMethod.POST);
                // 不使用jwt令牌时注释掉该项
                .accessTokenConverter(accessTokenConverter)
                .tokenStore(tokenStore)
                // userDetailsService必须配置, 否则refresh_token无法使用
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // tokenKeyAccess : 对/oauth/token_key 公开不拦截
        // checkTokenAccess : 对/oauth/check_token 公开不拦截, 防止资源服务器提交的token验证请求被拦截
        // allowFormAuthenticationForClients : 允许表单认证(通过表单申请令牌), 不建议使用, 会导致获取不到令牌
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }
}
