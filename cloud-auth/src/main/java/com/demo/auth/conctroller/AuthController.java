package com.demo.auth.conctroller;

import com.demo.auth.config.properties.AuthServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author thymi
 * @Date 2020/6/1
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    AuthServerProperties authServerProperties;

    /**
     * 授权码回调
     *
     * @param code
     * @return
     */
    @GetMapping("/redirect")
    public String redirect(String code) {
        return "得到授权码 : " + code;
    }

    /**
     * 公钥接口
     *
     * @return
     */
    @GetMapping("/pubkey")
    public String getKey() {
        Resource resource = new ClassPathResource("pubkey.txt");
        String publicKey = null;
        try {
            InputStream inputStream = resource.getInputStream();
            publicKey = IOUtils.toString(inputStream,"utf-8");
            inputStream.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return publicKey;
    }

    @GetMapping("/test")
    public String test(){
        log.info(authServerProperties.getPassword());
        return authorizationServerProperties.getJwt().getKeyStore();
    }

}
