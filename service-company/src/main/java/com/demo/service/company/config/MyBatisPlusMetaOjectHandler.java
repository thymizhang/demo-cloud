package com.demo.service.company.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatisPlus自动填充组件, 对应domain对象的注解@TableField(fill = FieldFill.INSERT_UPDATE)<br/>
 * 注意: 如果只是自动填充时间类字段, 不建议使用
 *
 * @Author thymi
 * @Date 2020/6/16
 */
@Component
public class MyBatisPlusMetaOjectHandler implements MetaObjectHandler {
    /**
     * 插入数据时, 填充指定字段
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String createdField = "created";
        String updatedField = "updated";
        // 如果存在某个字段才自动填充, 可以减少开销
        if (metaObject.hasSetter(createdField)) {
            setFieldValByName(createdField, LocalDateTime.now(), metaObject);
        }
        if (metaObject.hasSetter(updatedField)) {
            setFieldValByName(updatedField, LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 更新数据时, 填充指定字段
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updated", LocalDateTime.now(), metaObject);
    }
}
