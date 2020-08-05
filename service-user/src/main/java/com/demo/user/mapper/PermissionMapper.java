package com.demo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.common.model.user.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author thymi
 * @Date 2020/6/30
 */
@Repository(value = "permissionMapper")
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    @Select("select p.* from tb_user as u \n" +
            "left join tb_user_role as ur on u.id = ur.user_id\n" +
            "left join tb_role as r on r.id = ur.role_id\n" +
            "left join tb_role_permission as rp on r.id = rp.role_id\n" +
            "left join tb_permission as p on p.id = rp.permission_id\n" +
            "where u.id = #{userId}")
    List<Permission> selectByUserId(@Param("userId") Long userId);
}
