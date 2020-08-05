package com.demo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.common.model.user.RolePermission;
import org.springframework.stereotype.Repository;

/**
 * @Author thymi
 * @Date 2020/6/30
 */
@Repository(value = "rolePermissionMapper")
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
