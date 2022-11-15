package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.user.api.ResourceQueryPage;
import org.example.user.entity.vo.ResourceVO;

public interface ResourceService {
    Boolean addResource(ResourceVO resourceVo);

    Boolean deleteResource(String id);

    Boolean updateResource(ResourceVO resourceVo);

    Page<ResourceVO> getResourceList(ResourceQueryPage queryPage);
}