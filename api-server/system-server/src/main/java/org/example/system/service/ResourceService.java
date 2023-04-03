package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.system.api.ResourceQueryPage;
import org.example.system.entity.vo.ResourceVo;

public interface ResourceService {
    Boolean addResource(ResourceVo resourceVo);

    Boolean deleteResource(String id);

    Boolean updateResource(ResourceVo resourceVo);

    Page<ResourceVo> getResourceList(ResourceQueryPage queryPage);
}