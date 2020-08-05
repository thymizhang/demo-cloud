package com.demo.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.user.User;
import com.demo.user.mapper.UserMapper;
import com.demo.user.service.UserServiceApi;
import org.springframework.stereotype.Service;

/**
 * @Author thymi
 * @Date 2020/6/15
 */
@Service(value = "userService")
public class UserServiceApiImpl extends ServiceImpl<UserMapper, User> implements UserServiceApi {

    /**
     * 使用方法拦截器插件, 来自动完成读写分离, 默认数据库名为master和slave
     * @return
     */
//    @Bean
//    public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin(){
//        return new MasterSlaveAutoRoutingPlugin();
//    }
}
