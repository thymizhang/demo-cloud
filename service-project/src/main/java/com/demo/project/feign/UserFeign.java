package com.demo.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用userService
 *
 *
 * @Primary 用于解决There is more than one bean of 'xxx' type的问题你
 * @Author thymi
 * @Date 2020/5/13
 */
@FeignClient(value = "service-user", fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeign {

    /**
     * 获取用户姓名
     *
     * @return 用户姓名
     */
    @GetMapping("/user")
    public String getUser();

    /**
     * 获取用户项目
     *
     * @return 用户项目
     */
    @GetMapping("/user/project")
    public String getProject();
}
