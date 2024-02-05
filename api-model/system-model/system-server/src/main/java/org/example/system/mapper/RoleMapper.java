package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.system.entity.Role;
import org.example.system.query.RoleQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getRoleList(Page<Role> page, @Param("queryPage") RoleQueryPage queryPage);
}