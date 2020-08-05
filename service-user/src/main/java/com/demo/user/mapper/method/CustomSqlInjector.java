package com.demo.user.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;

import java.util.List;

/**
 * 自定义mapper方法注入类, 将所有的Method添加到mapper中
 *
 * @Author thymi
 * @Date 2020/6/17
 */
//@Component
public class CustomSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass){
        // 先取回原来的方法列表
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 将自定义method添加到方法列表
        methodList.add(new SyncCreatorMethod());
        // 添加选装件, 根据 id 逻辑删除数据,并带字段填充功能
        methodList.add(new LogicDeleteByIdWithFill());
        // 添加选装件, 根据 ID 更新固定的那几个字段(但是不包含逻辑删除), 案例: 不更新name字段
        methodList.add(new AlwaysUpdateSomeColumnById(m -> !m.getColumn().equals("name")));

        return methodList;
    }
}
