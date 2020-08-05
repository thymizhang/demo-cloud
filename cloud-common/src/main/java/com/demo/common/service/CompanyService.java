package com.demo.common.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author thymi
 * @Date 2020/5/25
 */
@RequestMapping("/company")
public interface CompanyService {

    /**
     * 获取公司信息
     *
     * @return 公司信息
     */
    @GetMapping("/view")
    public String getCompany();

    /**
     * 获取公司成员
     *
     * @return 公司成员
     */
    @GetMapping("/user")
    public String getCompanyUser();
}
