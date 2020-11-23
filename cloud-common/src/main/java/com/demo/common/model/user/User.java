package com.demo.common.model.user;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @Author thymi
 * @Date 2020/6/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_user")
public class User {

    /**
     * 当主键字段不是id时使用@TableId
     * 主键策略:ASSIGN_ID:数值,雪花算法 ASSIGN_UUID:字符串,
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 指定数据库中对应的字段@TableField
     *
     */
    @TableField(value = "name")
    private String username;

    private String password;

    /**
     * 符合驼峰规则,数据库为user_age
     */
    private Integer userAge;

    private String email;

    /**
     * condition: 使用实体作为查询条件时,设置条件匹配的方式,默认是eq(等于)
     */
    @TableField(condition = SqlCondition.LIKE)
    private String phone;

    /**
     * @Version 用于乐观锁场景,需要在数据库中设置默认值
     */
    @Version
    private Integer version;

    /**
     * 配置逻辑删除
     * 1 在yml中配置logic-not-delete-value和logic-delete-value
     * 2 在字段配置@TableLogic注解, 如果全局配置了, 这里可以省略
     * 3 在字段配置@TableField注解, 查询不返回deleted字段, 注意:对自定义sql语句无效,不仅会返回deleted字段而且不会加上`deleted=0`条件
     * 4 数据库deleted字段需要设置默认值为0
     */
    @TableField(select = false)
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * FieldFill.INSERT: 在插入时自动填充时间
     * 注意需要配置MetaObjectHandler实现
     * 时间类的自动填充建议使用数据库设置会好些<br/>
     * LocalDateTime格式存储到redis需要指定序列化方式
     * @JsonDeserialize @JsonSerialize @JsonFormat
     */
//    @TableField(fill = FieldFill.INSERT)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * FieldFill.UPDATE: 在更新时自动填充时间
     * 注意需要配置MetaObjectHandler实现
     * 时间类的自动填充建议使用数据库设置会好些 <br/>
     * LocalDateTime格式存储到redis需要指定序列化方式
     * @JsonDeserialize @JsonSerialize @JsonFormat
     */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    /**
     * 有些信息用于返回给前端,不是数据库的字段,有三种方式<br/>
     * 1 private transient ,缺点:不能序列化;<br/>
     * 2 private static , 缺点:静态化需要单独写get set;<br/>
     * 3 @TableField(exist = false) , 推荐.
     */
    @TableField(exist = false)
    private String remark;

}

