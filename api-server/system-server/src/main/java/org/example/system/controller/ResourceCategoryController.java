package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.system.vo.ResourceCategoryVo;
import org.example.common.model.CommonResult;
import org.example.system.api.ResourceCategoryQueryPage;
import org.example.system.service.ResourceCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@RestController
@RequestMapping("/resourceCategory")
public class ResourceCategoryController {
    @Resource
    private ResourceCategoryService resourceCategoryService;

    /**
     * 新增资源分类信息
     *
     * @param resourceCategoryVo
     * @return
     */
    @RequestMapping("/add")
    public CommonResult<ResourceCategoryVo> addResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.addResourceCategory(resourceCategoryVo));
    }

    /**
     * 删除资源分类信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public CommonResult<Boolean> deleteResourceCategory(@RequestParam String id) {
        return CommonResult.success(resourceCategoryService.deleteResourceCategory(id));
    }

    /**
     * 更新资源分类
     *
     * @param resourceCategoryVo
     * @return
     */
    @RequestMapping("/update")
    public CommonResult<Boolean> updateResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.updateResourceCategory(resourceCategoryVo));
    }

    /**
     * 获取资源分类列表
     *
     * @param queryPage
     * @return
     */
    @RequestMapping("/list")
    public CommonResult<Page<ResourceCategoryVo>> getResourceCategoryList(@RequestBody ResourceCategoryQueryPage queryPage) {
        return CommonResult.success(resourceCategoryService.getResourceCategoryList(queryPage));
    }

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    @RequestMapping("/list/all")
    public CommonResult<List<ResourceCategoryVo>> getAllResourceCategoryList() {
        return CommonResult.success(resourceCategoryService.getAllResourceCategoryList());
    }
}