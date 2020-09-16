package com.demo.project.feign;

import com.demo.common.service.CompanyService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 正确的feign使用方式, 通过接口给调用方和被调用方同时使用, 保证一致性
 *
 * @Author thymi
 * @Date 2020/5/25
 */
@FeignClient(value = "service-company", fallbackFactory = CompanyFeignFallbackFactory.class)
public interface CompanyFeign extends CompanyService {

}
