package org.example.user.service.cache;

import org.example.user.entity.Resource;

import java.util.List;

public interface ResourceCacheService {
    List<Resource> getResources();

    void setResource(List<Resource> resources);
}