package com.demo.api.controller;

import com.demo.api.feign.UserFeign;
import com.demo.common.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务接口
 *
 * @Author thymi
 * @Date 2020/7/3
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    ResourceServerProperties resourceServerProperties;

    @GetMapping("/one/{name}")
    //@PreAuthorize("hasAnyRole('admin', 'dingding')")
    @PreAuthorize("hasAuthority('System')")
    public User getOne(@PathVariable("name") String name){
        User user = userFeign.selectOne(name);
        return user;
    }

    @GetMapping("/jwt")
    public String jwt(){
        return resourceServerProperties.getResourceId();
    }

}
