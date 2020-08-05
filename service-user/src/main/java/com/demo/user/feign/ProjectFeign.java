package com.demo.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用projectService
 *
 * @Author thymi
 * @Date 2020/5/13
 */
@FeignClient(value = "service-project", fallbackFactory = ProjectFeignFallbackFactory.class)
public interface ProjectFeign {

    /**
     * 获取项目信息
     *
     * @return 项目信息
     */
    @GetMapping("/project")
    String getProject();

}
