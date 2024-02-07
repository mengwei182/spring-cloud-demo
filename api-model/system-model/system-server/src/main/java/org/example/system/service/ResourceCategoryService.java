package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.system.query.ResourceCategoryQueryPage;
import org.example.system.entity.vo.ResourceCategoryVO;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface ResourceCategoryService {
    /**
     * 新增资源分类信息
     *
     * @param resourceCategoryVO
     * @return
     */
    ResourceCategoryVO addResourceCategory(ResourceCategoryVO resourceCategoryVO);

    /**
     * 删除资源分类信息
     *
     * @param id
     * @return
     */
    Boolean deleteResourceCategory(String id);

    /**
     * 更新资源分类
     *
     * @param resourceCategoryVO
     * @return
     */
    Boolean updateResourceCategory(ResourceCategoryVO resourceCategoryVO);

    /**
     * 分页获取资源分类列表
     *
     * @param queryPage
     * @return
     */
    Page<ResourceCategoryVO> getResourceCategoryList(ResourceCategoryQueryPage queryPage);

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    List<ResourceCategoryVO> getAllResourceCategoryList();

    /**
     * 根据资源分类名称查询资源分类信息
     *
     * @param name
     * @return
     */
    ResourceCategoryVO getResourceCategoryByName(String name);
}