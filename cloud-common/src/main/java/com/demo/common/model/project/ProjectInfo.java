package com.demo.common.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 项目信息
 *
 * @Author thymi
 * @Date 2020/7/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String simName;

    @TableField(select = false)
    private Integer deleted;

    @TableField(exist = false)
    private String port;

}
