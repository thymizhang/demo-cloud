package com.demo.service.company.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.demo.common.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 公司服务
 *
 * @Author thymi
 * @Date 2020/5/25
 */
@RestController
@RefreshScope
@Slf4j
public class CompanyServiceController implements CompanyService {
    @Override
    public String getCompany() {
        log.info("公司服务接口getCompany被调用...");
        return "云联万企";
    }

    /**
     * sentinel熔断回调
     * @return
     */
    @Override
    //@SentinelResource(value = "/company/user",blockHandler = "handleBlockException")
    public String getCompanyUser() {
        log.info("公司服务接口getCompanyUser被调用...");
        try {
            // 测试sentinel熔断降级
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "thymi sara ...";
    }

    public String handleBlockException(String str, BlockException ex){
        return " 服务降级处理："+str+"  异常为："+ex.getClass().getSimpleName();
    }

}
