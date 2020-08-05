package com.demo.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.demo.common.model.project.ProjectInfo;
import com.demo.common.service.ProjectService;
import com.demo.project.feign.CompanyFeign;
import com.demo.project.feign.UserFeign;
import com.demo.project.mapper.ProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @RefreshScope 动态加载nacos中的配置文件
 * @ConditionalOnBean 在某个bean存在的情况下再加载
 *
 * @Author thymi
 * @Date 2020/5/11
 */
@RestController
@Slf4j
@RefreshScope
public class ProjectServiceImpl implements ProjectService {

    /**
     * spring的上下文环境配置
     */
    @Autowired
    ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    UserFeign userFeign;

    @Autowired
    CompanyFeign companyFeign;

    @Value("${jdbc.driver}")
    String driver;

    @Value("${server.port}")
    String port;

    @Autowired
    ProjectMapper projectMapper;


    /**
     * 实时获取配置文件更新
     *
     * @return
     */
    @GetMapping("/getConfigs")
    public String getConfigs(){
        return configurableApplicationContext.getEnvironment().getProperty("spring.application.name");
    }

    @GetMapping("/get")
    public String get(){
        log.info("Controller调用成功");
        return port;
    }

    @GetMapping("/port")
    public String getProject(){
        log.info("项目服务被调用...");
        return port;
    }

    @GetMapping("/project/user/{args}")
    public String getUser(@PathVariable String args) {
        log.info("调用用户服务...");
        return port + " | " + userFeign.getUser() + " | " + args;
    }

    @GetMapping("/project/users")
    public String getUserProject(){
        log.info("调用用户服务并回调...");
        return userFeign.getProject();
    }

    @GetMapping("/project/company")
    public String getProjectCompany(){
        log.info("调用公司服务...");
        return port + " | " + companyFeign.getCompany();
    }

    @Override
    public List<ProjectInfo> getProjectName(String name) {
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(ProjectInfo::getId, ProjectInfo::getName, ProjectInfo::getSimName)
                .like(ProjectInfo::getName, name);
        List<ProjectInfo> projectInfos = projectMapper.selectList(lambdaQueryWrapper);
        projectInfos.forEach(projectInfo -> projectInfo.setPort(port));
        return projectInfos;
    }

    @Override
    public List<ProjectInfo> getAllProjects() {
        List<ProjectInfo> projectInfos = projectMapper.selectList(null);
        projectInfos.forEach(projectInfo -> projectInfo.setPort(port));
        return projectInfos;
    }
}
