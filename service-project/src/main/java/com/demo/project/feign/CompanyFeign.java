package com.demo.project.feign;

import com.demo.common.service.CompanyService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author thymi
 * @Date 2020/5/25
 */
@FeignClient(value = "service-company", fallbackFactory = CompanyFeignFallbackFactory.class)
public interface CompanyFeign extends CompanyService {

}
