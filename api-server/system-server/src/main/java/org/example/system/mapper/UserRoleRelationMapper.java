package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.system.entity.UserRoleRelation;

@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
}