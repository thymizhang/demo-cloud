package com.demo.common.model.user;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户权限
 *
 * @Author thymi
 * @Date 2020/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_permission")
public class Permission {

    private Long id;
    private Long parentId;
    private String name;
    private String enname;
    private String url;
    private String description;

}
