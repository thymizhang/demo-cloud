package com.demo.user.feign;

import feign.hystrix.FallbackFactory;

/**
 * @Author thymi
 * @Date 2020/5/25
 */
public class ProjectFeignFallbackFactory implements FallbackFactory<ProjectFeign> {
    @Override
    public ProjectFeign create(Throwable cause) {
        return new ProjectFeign() {
            @Override
            public String getProject() {
                return "项目服务忙...";
            }
        };
    }
}
