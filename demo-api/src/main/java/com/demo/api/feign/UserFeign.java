package com.demo.api.feign;

import com.demo.common.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author thymi
 * @Date 2020/7/3
 */
@FeignClient(value = "service-user")
public interface UserFeign extends UserService {
}
