package com.demo.common.model.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限
 *
 * @Author thymi
 * @Date 2020/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_role_permission")
public class RolePermission {

    private Long id;
    private Long roleId;
    private Long permissionId;

}
