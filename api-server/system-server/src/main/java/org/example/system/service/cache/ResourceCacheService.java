package org.example.system.service.cache;

import org.example.common.entity.system.Resource;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface ResourceCacheService {
    List<Resource> getResources();

    void setResource(List<Resource> resources);
}