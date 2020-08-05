package com.demo.api.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.demo.api.feign.ProjectFeign;
import com.demo.api.feign.UserFeign;
import com.demo.common.model.project.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author thymi
 * @Date 2020/7/3
 */
@RestController
@RequestMapping("/api/project")
@Slf4j
//@EnableOAuth2Sso
public class ProjectController {

    @Autowired
    private ProjectFeign projectFeign;

    @Autowired
    private UserFeign userFeign;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('System')")
    @SentinelResource(value = "/api/project/all", blockHandler = "handleBlockException")
    public List<ProjectInfo> test() {
        List<ProjectInfo> allProjects = projectFeign.getAllProjects();
        String port = userFeign.getPort();
        allProjects.forEach(projectInfo -> {
            String port1 = projectInfo.getPort();
            projectInfo.setPort(port1 + " : " + port);
        });
        return allProjects;
    }

    public String handleBlockException(String str, BlockException ex) {
        return " 服务降级处理：" + str + "  异常为：" + ex.getClass().getSimpleName();
    }
}
