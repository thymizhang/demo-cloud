package com.demo.api.feign;

import com.demo.common.service.CompanyService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author thymi
 * @Date 2020/7/3
 */
@FeignClient(value = "service-company")
public interface CompanyFeign extends CompanyService {
}
