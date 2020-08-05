package com.demo.common.model.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色
 *
 * @Author thymi
 * @Date 2020/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_user_role")
public class UserRole {

    private Long id;
    private Long userId;
    private Long roleId;

}
