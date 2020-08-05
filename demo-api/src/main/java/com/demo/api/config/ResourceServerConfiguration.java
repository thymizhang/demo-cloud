package com.demo.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OAuth2资源服务器配置<br/>
 * 1 资源服务器id设置;<br/>
 * 2 令牌检查策略;<br/>
 * 3 拦截规则;<br/>
 *
 * @Author thymi
 * @Date 2020/7/6
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    /**
     * 直接装载, 无需注入
     */
    @Autowired
    ResourceServerProperties resourceServerProperties;

    /**
     * 需要通过@Bean注入
     */
    @Autowired
    AuthorizationServerProperties authorizationServerProperties;

    @Bean
    public AuthorizationServerProperties authorizationServerProperties() {
        return new AuthorizationServerProperties();
    }
//
//    @Bean
//    public  ResourceServerProperties resourceServerProperties(){
//        return new ResourceServerProperties();
//    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey;
        //读取认证服务器上的公钥
        //publicKey = new RestTemplate().getForObject(resourceServerProperties.getJwt().getKeyValue(), String.class);

        //读取本地公钥, 统一放在common工程中
        Resource resource = new ClassPathResource("pubkey.txt");
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

    /**
     * 从本地文件获取公钥
     *
     * @return
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource("pubkey.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            System.out.println("本地公钥");
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return getPubKeyFromServer();
        }
    }

    /**
     * 从授权服务器获取公钥
     *
     * @return
     */
    private String getPubKeyFromServer() {
        ObjectMapper objectMapper = new ObjectMapper();
        String pubKey = new RestTemplate().getForObject(resourceServerProperties.getJwt().getKeyValue(), String.class);
        //String pubKey ="";
        try {
            Map map = objectMapper.readValue(pubKey, Map.class);
            System.out.println("联网公钥");
            return map.get("value").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public ResourceServerTokenServices defaultTokenServices(){
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        defaultTokenServices.setTokenStore(jwtTokenStore());
        return defaultTokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        /**
         * resourceId : 资源id, 可以在配置文件中指定, 服务器端指定方式: clients.inMemory().resourceIds()
         * tokenServices : 验证令牌的服务,可以在配置文件中配置远程校验参数,如果在yml中配置,这里可省略
         */
        resources
                //.tokenServices(resourceServerTokenServices())
                // jwt令牌如果不设置该项, 默认去认证服务器校验token, 测试有问题?
                .tokenStore(jwtTokenStore())
                .tokenServices(defaultTokenServices())
                .resourceId("demo-api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         * antMatchers : /api/** 匹配通配符路径, 应该从数据库获取
         * access : #oauth2.hasAnyScope('app'), 指定令牌的scope
         */
        http.authorizeRequests()
                .antMatchers("/api/**").access("#oauth2.hasAnyScope('app','web')")
                // 关闭csrf
                .and().csrf().disable()
                // 不再记录session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
