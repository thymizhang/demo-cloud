package com.demo.user.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * SQL注入器, 自定义mapper方法
 *
 * 1 定义method类;
 * 2 重写SqlInjector, 将method加入列表;
 * 3
 *
 * 更新creator姓名, 当用户修改姓名后, 需要更新该用户创建的某些资源, 可调用该方法进行姓名更新
 *
 * @Author thymi
 * @Date 2020/6/17
 */
public class SyncCreatorMethod extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "update " + tableInfo.getTableName() + " set creator = ";
        // mapper接口方法名
        String methodName = "syncCreator";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        // 根据你的语句选择update还是delete
        return addUpdateMappedStatement(mapperClass,modelClass,methodName,sqlSource);
    }
}
