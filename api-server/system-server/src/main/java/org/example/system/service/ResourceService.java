package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.cache.CacheService;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.system.api.ResourceQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface ResourceService extends CacheService {
    /**
     * 新增资源
     *
     * @param resourceVo
     * @return
     */
    Boolean addResource(ResourceVo resourceVo);

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    Boolean deleteResource(String id);

    /**
     * 更新资源
     *
     * @param resourceVo
     * @return
     */
    Boolean updateResource(ResourceVo resourceVo);

    /**
     * 分页获取资源列表
     *
     * @param queryPage
     * @return
     */
    Page<ResourceVo> getResourceList(ResourceQueryPage queryPage);

    /**
     * 获取全部资源列表
     *
     * @return
     */
    List<ResourceVo> getAllResourceList();

    /**
     * 根据id获取资源详情
     *
     * @return
     */
    ResourceVo getResourceById(String id);

    /**
     * 根据url和分类id查询资源信息
     *
     * @param url
     * @param categoryId
     * @return
     */
    ResourceVo getResource(String url, String categoryId);

    /**
     * 刷新所有系统中所有资源
     */
    void refreshResource();

    /**
     * 根据用户id获取资源列表
     *
     * @param userId
     * @return
     */
    List<ResourceVo> getResourceByUserId(String userId);
}