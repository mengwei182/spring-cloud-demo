package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.example.system.api.ResourceQueryPage;
import org.example.system.entity.Resource;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> getResourceList(IPage<Resource> page, ResourceQueryPage queryPage);
}