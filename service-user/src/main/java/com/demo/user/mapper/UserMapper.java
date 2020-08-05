package com.demo.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.model.user.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author thymi
 * @Date 2020/6/30
 */
@Repository(value = "userMapper")
public interface UserMapper extends BaseMapper<User> {

    /**
     * 自定义sql(可传条件)
     *
     * @param wrapper
     * @return
     */
    @Select("select * from tb_user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);


    /**
     * 自定义分页sql(可传条件)
     *
     * @param page
     * @param wrapper
     * @return
     */
    @Select("select * from tb_user ${ew.customSqlSegment}")
    IPage<User> selectAllPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
}
