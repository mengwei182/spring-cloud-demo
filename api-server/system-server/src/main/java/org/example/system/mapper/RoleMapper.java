package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.system.entity.Role;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getRolesByUserId(String userId);
}