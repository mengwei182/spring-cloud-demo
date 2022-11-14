package org.example.user.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.dubbo.userserver.ResourceDubboService;
import org.example.dubbo.userserver.entity.Resource;
import org.example.user.service.cache.ResourceCacheService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李辉
 * @since 2022/11/11
 */
@Service
@DubboService(interfaceClass = ResourceDubboService.class, timeout = 10000)
public class ResourceDubboServiceImpl implements ResourceDubboService {
    @javax.annotation.Resource
    private ResourceCacheService resourceCacheService;

    @Override
    public List<Resource> getResources() {
        List<Resource> resultResources = new ArrayList<>();
        List<org.example.user.entity.Resource> resources = resourceCacheService.getResources();
        for (org.example.user.entity.Resource resource : resources) {
            Resource resultResource = new Resource();
            BeanUtils.copyProperties(resource, resultResource);
            resultResources.add(resultResource);
        }
        return resultResources;
    }
}