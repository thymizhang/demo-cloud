package com.demo.auth.config.service;


import com.demo.auth.feign.UserFeign;
import com.demo.common.model.user.Permission;
import com.demo.common.model.user.User;
import com.demo.common.util.data.JsonUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义用户身份接口实现, 并在WebSecurityConfiguration的configure中使用
 * <p>
 * 在Security中，角色和权限共用GrantedAuthority接口，唯一的不同角色就是多了个前缀"ROLE_"
 *
 * @Author thymi
 * @Date 2020/6/18
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeign userFeign;

    /**
     * 从业务数据库中找到user, 并与UserDetails进行对接
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFeign.selectOne(username);
        // OAuth2的权限集合
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        // 如果user存在, 获取user权限
        if (user.getId() != null) {
            List<Permission> permissions = userFeign.selectPermissionByUserId(Long.valueOf(user.getId()));
            if (permissions.size() > 0) {
                permissions.forEach(permission -> {
                    // 所谓权限，就是一个个字符串, 获取用户的权限字符串, 然后放入OAuth2的权限集合, 让OAuth2知道你的系统有哪些权限
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getEnname());
                    grantedAuthorities.add(grantedAuthority);
                });
            }
        } else {
            // 如果查不到用户, 返回null, 由provider来抛出异常
            return null;
        }
        //如果需要让资源服务器获取更多的信息,可以将其他信息拼接起来,保存到username中,通过username传递给资源服务器
        //String userInfo = user.getUsername();
        String userInfo = JsonUtil.toJsonString(user);
        //String userInfo = user.getId() + "|" + user.getUsername();

        // 注意这里返回的是系统的User对象
        org.springframework.security.core.userdetails.User systemUser = new org.springframework.security.core.userdetails.User(userInfo, user.getPassword(), grantedAuthorities);
        return systemUser;
    }
}
