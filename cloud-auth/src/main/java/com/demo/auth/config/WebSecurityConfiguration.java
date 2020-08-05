package com.demo.auth.config;

import com.demo.auth.config.properties.AuthServerProperties;
import com.demo.auth.config.service.JwtTokenEnhancer;
import com.demo.auth.config.service.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;

/**
 * 服务器安全配置<br/>
 * 为了方便注入, 这里将`DataSource`、`UserDetailsService`、`AuthenticationManager`都放在这里加载<br/>
 *
 * @EnableGlobalMethodSecurity(securedEnabled=true) 开启@Secured 注解是用来定义业务方法的安全配置,缺点是不支持Spring EL表达式。并且指定的角色必须以ROLE_开头，不可省略。<br/>
 * @EnableGlobalMethodSecurity(prePostEnabled=true) 使用表达式实现方法级别的安全性, 提供了基于表达式的访问控制, 以下4个注解可用<br/>
 * - @PreAuthorize 进入方法之前验证授权,基于表达式的计算结果来限制对方法的访问<br/>
 * - @PostAuthorize 在方法执行后再进行权限验证,适合验证带有返回值的权限。Spring EL 提供 返回对象能够在表达式语言中获取返回的对象returnObject。<br/>
 * - @PreFilter 对集合类型的参数执行过滤，移除结果为false的元素<br/>
 * - @PostFilter 对集合类型的返回值进行过滤，移除结果为false的元素<br/>
 * @EnableGlobalMethodSecurity(jsr250Enabled=true) 以下3个注解可用 <br/>
 * - @DenyAll 拒绝所有访问<br/>
 * - @PermitAll 允许所有访问<br/>
 * - @RolesAllowed({"USER", "ADMIN"}) 该方法只要具有"USER", "ADMIN"任意一种权限就可以访问。这里可以省略前缀ROLE_，实际的权限可能是ROLE_ADMIN<br/>
 * @Author thymi
 * @Date 2020/5/28
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 加载数据源
     *
     * @return
     * @Primary 最高优先级
     * @ConfigurationProperties 因为有默认配置, 需要指定配置, 这里指定配置文件中的datasource
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 加载服务器配置, 部分来自yml
     */
    @Bean
    public AuthorizationServerProperties authorizationServerProperties(){
        return new AuthorizationServerProperties();
    }

    /**
     * 查询用户信息服务, 获取密码、权限等信息。
     *
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // 将用户信息保存在内存, 注意:在config方法中也可以将用户信息保存在内存: AuthenticationManagerBuilder.inMemoryAuthentication().withUser().password().roles()
        //InMemoryUserDetailsManager service = new InMemoryUserDetailsManager();
        //service.createUser(User.withUsername("thymi").password("111111").authorities("").build());
        //service.createUser(User.withUsername("sara").password("111111").authorities("").build());

        // 采用自定义(调用微服务)实现
        UserDetailsService service = new UserDetailsServiceImpl();
        return service;
    }

    /**
     * 认证管理器, 采用`password`授权类型时需要配置。
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * OAuth的默认加密方式 : 采用SHA-256 +随机盐+密钥对密码进行加密<br/>
     * 两个地方需要用到:<br/>
     * 1 AuthorizationConfiguration在内存配置client密码(secret)时加密;<br/>
     * 2 WebSecurityConfiguration在内存配置用户密码时加密;<br/>
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取公钥配置
     *
     * @return
     */
    @Bean
    public AuthServerProperties authServerProperties() {
        return new AuthServerProperties();
    }

    /**
     * JWT令牌转换器
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // 对称加密, 资源服务器端需要使用相同的密码
        //converter.setSigningKey("gceasy");

        // 非对称加密,创建密钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(authorizationServerProperties().getJwt().getKeyStore()), authorizationServerProperties().getJwt().getKeyPassword().toCharArray());
        converter.setKeyPair( keyStoreKeyFactory.getKeyPair(authorizationServerProperties().getJwt().getKeyAlias()));

        return converter;
    }

    /**
     * JWT令牌增强器
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    /**
     * 令牌存储策略<br/>
     * tokenStore()有三种实现:<br/>
     * 1 InMemoryTokenStore : 默认, 通过内存管理令牌;<br/>
     * 2 JdbcTokenStore : 通过数据库存储令牌;<br/>
     * 3 JwtTokenStore : 可以对令牌编码,对于后端服务来说,不需要存储,无需远程校验.缺点:撤销一个已授权的令牌很难,暂用空间大,系统消耗大.<br/>
     * 关于令牌存储的问题:<br/>
     * JWT的优点是您无需每次都在db中检查令牌，因为您可以使用加密技术来验证令牌是否合法, 所以JWT不需要存储到数据库.<br/>
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        // 内存模式, 生成OAuth2默认令牌
        //return new InMemoryTokenStore();

        // 数据库管理令牌
        //return new JdbcTokenStore(dataSource());

        // jwt令牌
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 将公钥接口暴露给网关访问
        http.authorizeRequests()
                .antMatchers("/auth/pubkey").permitAll()
                .antMatchers("/auth/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    /**
     * 指定认证方式
     * 1 内存认证方式;
     * 2 自定认证方式;
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 1 内存认证方式, 密码需要使用BCryptPasswordEncoder加密
        //        auth.inMemoryAuthentication()
        //                .withUser("thymi").password(passwordEncoder().encode("654321")).roles("USER")
        //                .and()
        //                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN");

        // 2 自定义认证方式
        auth.userDetailsService(userDetailsService());

    }
}
