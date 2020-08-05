package com.demo.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.demo.common.dao.ResponseResult;
import com.demo.common.model.user.Permission;
import com.demo.common.model.user.User;
import com.demo.common.service.UserService;
import com.demo.user.feign.ProjectFeign;
import com.demo.user.mapper.PermissionMapper;
import com.demo.user.mapper.UserMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author thymi
 * @Date 2020/5/13
 */
@RestController
@RefreshScope
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    ProjectFeign projectFeign;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Value("${server.port}")
    String port;

    @GetMapping("/project")
    public String getProject() {
        log.info("调用项目服务...");
        return port + " | " + projectFeign.getProject();
    }

    @Override
    public String getPort() {
        //        try {
        //            TimeUnit.SECONDS.sleep(1);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        return port;
    }

    @Override
    public ResponseResult<User> selectUserOne(String name) {

        if (name == null) {
            return null;
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getUsername, name);

        // 强制使用主库(主数据库)
        HintManager.getInstance().setMasterRouteOnly();
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        return new ResponseResult<>(Integer.valueOf(HttpStatus.OK.value()), HttpStatus.OK.toString(), user);
    }

    @Override
    public User selectOne(String name) {
        if (name == null) {
            return null;
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.eq(User::getUsername, name);

        // 强制使用主库(主数据库)
        //HintManager.getInstance().setMasterRouteOnly();
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        return user;
    }

    @Override
    public List<Permission> selectPermissionByUserId(Long UserId) {
        if (UserId == null){
            return Lists.newArrayList();
        }
        return permissionMapper.selectByUserId(UserId);
    }
}
