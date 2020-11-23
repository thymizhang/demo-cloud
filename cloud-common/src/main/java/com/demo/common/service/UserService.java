package com.demo.common.service;

import com.demo.common.dao.ResponseResult;
import com.demo.common.model.user.Permission;
import com.demo.common.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户服务
 *
 * @Author thymi
 * @Date 2020/5/13
 */
@RequestMapping("/user")
public interface UserService {

    /**
     * 获取用户姓名
     *
     * @return 用户姓名
     */
    @GetMapping("/port")
    String getPort();

    /**
     * redis测试接口
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/redis/{username}")
    String getRedis(@PathVariable("username") String username);

    /**
     * 获取唯一用户, 统一对象返回
     *
     * @param name
     * @return 统一对象
     */
    @GetMapping("/onlyone/{name}")
    ResponseResult<User> selectUserOne(@PathVariable("name") String name);

    /**
     * 获取唯一用户
     *
     * @param name
     * @return 用户对象
     */
    @GetMapping("/one/{name}")
    User selectOne(@PathVariable("name") String name);

    /**
     * 获取用户权限
     *
     * @param UserId
     * @return
     */
    @GetMapping("/permission/{UserId}")
    List<Permission> selectPermissionByUserId(@PathVariable("UserId") Long UserId);
}
