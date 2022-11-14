package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.user.api.ResourceQueryPage;
import org.example.user.entity.vo.ResourceVo;

public interface ResourceService {
    Boolean addResource(ResourceVo resourceVo);

    Boolean deleteResource(String id);

    Boolean updateResource(ResourceVo resourceVo);

    Page<ResourceVo> getResourceList(ResourceQueryPage queryPage);
}