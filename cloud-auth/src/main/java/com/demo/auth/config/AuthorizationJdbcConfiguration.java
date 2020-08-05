package com.demo.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于数据库的认证服务器<br/>
 * 新建两张表, 用于存放客户端详情和授权码, 防止单点问题:<br/>
 * `oauth_client_details`、`oauth_code`
 * <p>
 * 请求授权码(内存): http://localhost:8080/oauth/authorize?client_id=client&response_type=code&scope=app&redirect_uri=http://localhost:8080/auth/redirect <br/>
 * 请求授权码(数据库): http://localhost:8080/oauth/authorize?client_id=client&response_type=code <br/>
 * 请求令牌(内存): http://localhost:8080/oauth/token 参数: client_id、client_secret、grant_type、code、redirect_uri <br/>
 * 请求令牌(数据库): http://localhost:8080/oauth/token 参数: grant_type、code <br/>
 * <br/>
 * 三大核心配置: <br/>
 * 1 ClientDetailsServiceConfigurer : 客户端详情初始化;<br/>
 * 2 AuthorizationServerEndpointsConfigurer : 配置令牌的访问端点和令牌服务;<br/>
 * 3 AuthorizationServerSecurityConfigurer : 配置令牌端点的安全约束;<br/>
 * <br/>
 *
 * @Author thymi
 * @Date 2020/6/1
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationJdbcConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 加密工具, 默认必须加载, 在WebSecurityConfiguration注入
     */
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 认证管理器, 在WebSecurityConfiguration注入
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * JDBC数据源, 在WebSecurityConfiguration注入
     */
    @Autowired
    DataSource dataSource;

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
     * 令牌增强器, 在WebSecurityConfiguration注入
     */
    @Autowired
    TokenEnhancer tokenEnhancer;

    /**
     * 用户详情服务, 在WebSecurityConfiguration注入
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * 客户端详情服务, 在AuthorizationJdbcConfiguration注入
     */
    @Autowired
    ClientDetailsService clientDetailsService;

    /**
     * 授权码服务, 在AuthorizationJdbcConfiguration注入
     */
    @Autowired
    AuthorizationCodeServices authorizationCodeServices;

    /**
     * 配置客户端详情服务
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService() {
        // 基于数据库存储客户端详情
        JdbcClientDetailsService service = new JdbcClientDetailsService(dataSource);
        //service.setPasswordEncoder(bCryptPasswordEncoder);
        return service;
    }

    /**
     * 配置令牌服务, ps: 这里没有用到
     *
     * @return
     */
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 客户端信息
        defaultTokenServices.setClientDetailsService(clientDetailsService());
        // 是否产生刷新令牌
        defaultTokenServices.setSupportRefreshToken(true);
        // 令牌存储策略
        defaultTokenServices.setTokenStore(tokenStore);
        // 令牌有效期4分钟, 这个参数还可以在configure里面进行配置clients.inMemory().withClient().accessTokenValiditySeconds()
        defaultTokenServices.setAccessTokenValiditySeconds(240);
        // 刷新令牌有效期3天, 这个参数还可以在configure里面进行配置clients.inMemory().withClient().refreshTokenValiditySeconds()
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);
        return defaultTokenServices;
    }

    /**
     * 配置授权码存储模式<br/>
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 内存模式(默认)
        //return new InMemoryAuthorizationCodeServices();

        // 数据库模式
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 用来配置客户端详情服务(ClientDetailsService),客户端详情信息在这里初始化
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置令牌的访问端点和令牌服务,其核心就是tokenStore和tokenServices.<br/>
     * 访问端点 : 可以理解申请令牌的url<br/>
     * 令牌服务 : 令牌的发放和生成<br/>
     * 授权是使用 AuthorizationEndpoint 这个端点来进行控制的，你能够使用 AuthorizationServerEndpointsConfigurer 这个对象的实例来进行配置(AuthorizationServerConfigurer 的一个回调配置项，见上的概述) ，如果你不进行设置的话，默认是除了资源所有者密码（password）授权类型以外，支持其余所有标准授权类型的（RFC6749）
     * <br/>
     * <p>
     * 令牌访问端点通过设定以下属性决定支持的授权类型(Grant_Types):<br/>
     * 1 authenticationManager : 认证管理器, 在WebSecurityConfiguration注入, 当选择`password`授权类型时, 需要注入一个AuthenticationManager对象;<br/>
     * 2 userDetailsService : 查询用户信息服务, 在WebSecurityConfiguration注入, 不论哪种模式都需要配置, 配置自己业务的用户登录校验和获取业务权限码;<br/>
     * 3 authorizationCodeServices : 用于`authorization_code`授权类型;<br/>
     * 4 implicitGrantService : 用于`implicit`授权类型;<br/>
     * 5 tokenGranter : 可以重写授权的过程, 一般不用, 否则用框架就没有意义了.<br/>
     * </p>
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // token增强器
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(tokenEnhancer);
        enhancers.add(accessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);

        endpoints
                .authorizationCodeServices(authorizationCodeServices)
                // AuthenticationManager必须配置, 否则会导致password模式无效
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                // tokenEnhancer增强器
                .tokenEnhancer(enhancerChain)
                // tokenServices(authorizationServerTokenServices())
                .accessTokenConverter(accessTokenConverter)
                // userDetailsService必须配置, 否则refresh_token无法使用
                .userDetailsService(userDetailsService);
    }

    /**
     * 用来配置令牌端点的安全约束<br/>
     * <p>
     * 令牌授权端点的url(Endpoint urls):<br/>
     * 1 /oauth/authorize : 授权端点;<br/>
     * 2 /oauth/token : 令牌端点;<br/>
     * 3 /oauth/comfirm_access : 用户确认授权提交端点;<br/>
     * 4 /oauth/error : 授权服务错误信息端点;<br/>
     * 5 /oauth/check_token : 用于资源服务访问的令牌解析端点;<br/>
     * 6 /oauth/token_key : 提供公钥的端点, 如果你使用JWT令牌的话.<br/>
     * <p>
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // tokenKeyAccess : 对/oauth/token_key 公开不拦截
        // checkTokenAccess : 对/oauth/check_token 公开不拦截, 防止资源服务器提交的token检查请求被拦截
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }
}
