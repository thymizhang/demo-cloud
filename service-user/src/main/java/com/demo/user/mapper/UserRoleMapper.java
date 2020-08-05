package com.demo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.common.model.user.UserRole;
import org.springframework.stereotype.Repository;

/**
 * @Author thymi
 * @Date 2020/6/30
 */
@Repository(value = "userRoleMapper")
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
