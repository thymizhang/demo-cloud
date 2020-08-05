package com.demo.auth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.io.Serializable;

/**
 * @Author thymi
 * @Date 2020/7/22
 */
@Data
@ConfigurationProperties(prefix = "auth.server")
public class AuthServerProperties implements Serializable {
    /**
     * 路径
     */
    private Resource keyPath;
    /**
     * 别名
     */
    private String alias;
    /**
     * 密码
     */
    private String secret;

    private String password;
}
