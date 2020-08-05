package com.demo.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.midi.Soundbank;

/**
 * @Author thymi
 * @Date 2020/7/23
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    ResourceServerProperties resourceServerProperties;

    @Autowired
    AuthorizationServerProperties authorizationServerProperties;

    @RequestMapping("/test")
    public String test(){
        System.out.println("resource-id : " + resourceServerProperties.getId());
        System.out.println("resource-key-value : " + resourceServerProperties.getJwt().getKeyValue());
        System.out.println("server- check-token-access: " + authorizationServerProperties.getCheckTokenAccess());
        //return resourceServerProperties.getJwt().getKeyUri();
        return "test";
    }
}
