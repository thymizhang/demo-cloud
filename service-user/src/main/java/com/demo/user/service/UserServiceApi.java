package com.demo.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.common.model.user.User;

/**
 * IService封装了Mapper中大部分方法, 用于形成service层
 * 好处是封装了一些常用的操作,包括批量操作
 * 注意: 这里我们只做参考, 实际不封装service层, 直接在controller调用mapper
 *
 * @Author thymi
 * @Date 2020/6/15
 */
public interface UserServiceApi extends IService<User> {
}
