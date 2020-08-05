package com.demo.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.common.model.project.ProjectInfo;
import org.springframework.stereotype.Repository;

/**
 * @Author thymi
 * @Date 2020/7/1
 */
@Repository(value = "projectMapper")
public interface ProjectMapper extends BaseMapper<ProjectInfo> {
}
