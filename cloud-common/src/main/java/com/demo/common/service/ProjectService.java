package com.demo.common.service;

import com.demo.common.model.project.ProjectInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 项目服务
 *
 * @Author thymi
 * @Date 2020/5/13
 */
@RequestMapping("/project")
public interface ProjectService {

    /**
     * 按名称搜索项目
     *
     * @param name
     * @return
     */
    @GetMapping("/name/{name}")
    List<ProjectInfo> getProjectName(@PathVariable("name") String name);

    /**
     * 获取所有项目
     *
     * @return
     */
    @GetMapping("all")
    List<ProjectInfo> getAllProjects();
}
