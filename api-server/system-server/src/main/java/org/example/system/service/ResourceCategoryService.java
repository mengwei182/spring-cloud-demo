package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.system.vo.ResourceCategoryVo;
import org.example.system.api.ResourceCategoryQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
public interface ResourceCategoryService {
    /**
     * 新增资源分类信息
     *
     * @param resourceCategoryVo
     * @return
     */
    Boolean addResourceCategory(ResourceCategoryVo resourceCategoryVo);

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
     * @param resourceCategoryVo
     * @return
     */
    Boolean updateResourceCategory(ResourceCategoryVo resourceCategoryVo);

    /**
     * 分页获取资源分类列表
     *
     * @param queryPage
     * @return
     */
    Page<ResourceCategoryVo> getResourceCategoryList(ResourceCategoryQueryPage queryPage);

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    List<ResourceCategoryVo> getAllResourceCategoryList();
}