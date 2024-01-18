package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.common.entity.system.Role;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}