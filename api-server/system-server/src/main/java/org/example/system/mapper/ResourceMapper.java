package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.system.Resource;
import org.example.system.api.ResourceQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    List<Resource> getResourceList(IPage<Resource> page, @Param("queryPage") ResourceQueryPage queryPage);
}