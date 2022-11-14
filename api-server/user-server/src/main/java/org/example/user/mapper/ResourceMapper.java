package org.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.example.user.api.ResourceQueryPage;
import org.example.user.entity.Resource;

import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> getResourceList(IPage<Resource> page, ResourceQueryPage queryPage);
}