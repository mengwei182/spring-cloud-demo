package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.system.entity.Department;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}