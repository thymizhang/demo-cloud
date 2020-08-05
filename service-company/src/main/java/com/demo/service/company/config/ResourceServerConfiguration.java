package com.demo.service.company.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 基于OAuth2的资源服务器配置, 保证所有的请求都会经过拦截器
 *
 * @Author thymi
 * @Date 2020/6/22
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 下面的antMatchers应该从数据库获取, /view/** 匹配通配符路径
        http.authorizeRequests()
                .antMatchers("/").hasAuthority("SystemCompany")
                .antMatchers("/view/**").hasAuthority("SystemCompanyView");
    }
}
