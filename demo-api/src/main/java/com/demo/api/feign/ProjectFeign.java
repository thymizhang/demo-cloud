package com.demo.api.feign;

import com.demo.common.service.ProjectService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author thymi
 * @Date 2020/7/3
 */
@FeignClient(value = "service-project")
public interface ProjectFeign extends ProjectService {
}
