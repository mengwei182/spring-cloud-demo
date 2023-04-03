package org.example.system.service.cache;

import org.example.system.entity.Resource;

import java.util.List;

public interface ResourceCacheService {
    List<Resource> getResources();

    void setResource(List<Resource> resources);
}