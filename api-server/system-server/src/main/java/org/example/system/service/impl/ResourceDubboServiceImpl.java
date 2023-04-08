package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.common.entity.system.Resource;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.dubbo.userserver.ResourceDubboService;
import org.example.system.service.cache.ResourceCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
@Service
@DubboService(interfaceClass = ResourceDubboService.class, timeout = 10000)
public class ResourceDubboServiceImpl implements ResourceDubboService {
    @javax.annotation.Resource
    private ResourceCacheService resourceCacheService;

    @Override
    public List<ResourceVo> getResources() {
        List<ResourceVo> resourceVos = new ArrayList<>();
        List<Resource> resources = resourceCacheService.getResources();
        for (Resource resource : resources) {
            ResourceVo resourceVO = new ResourceVo();
            BeanUtils.copyProperties(resource, resourceVO);
            resourceVos.add(resourceVO);
        }
        return resourceVos;
    }
}