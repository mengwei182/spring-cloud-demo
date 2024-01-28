package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.entity.system.vo.ResourceCategoryVo;
import org.example.common.model.CommonResult;
import org.example.system.api.ResourceCategoryQueryPage;
import org.example.system.service.ResourceCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Api(tags = "资源分类管理")
@RestController
@RequestMapping("/resourceCategory")
public class ResourceCategoryController {
    @Resource
    private ResourceCategoryService resourceCategoryService;

    /**
     * 新增资源分类
     *
     * @param resourceCategoryVo
     * @return
     */
    @ApiOperation("新增资源分类")
    @PostMapping("/add")
    public CommonResult<ResourceCategoryVo> addResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.addResourceCategory(resourceCategoryVo));
    }

    /**
     * 删除资源分类
     *
     * @param id
     * @return
     */
    @ApiOperation("删除资源分类")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteResourceCategory(@RequestParam String id) {
        return CommonResult.success(resourceCategoryService.deleteResourceCategory(id));
    }

    /**
     * 更新资源分类
     *
     * @param resourceCategoryVo
     * @return
     */
    @ApiOperation("更新资源分类")
    @PutMapping("/update")
    public CommonResult<Boolean> updateResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.updateResourceCategory(resourceCategoryVo));
    }

    /**
     * 获取资源分类列表
     *
     * @param queryPage
     * @return
     */
    @ApiOperation("获取资源分类列表")
    @GetMapping("/list")
    public CommonResult<Page<ResourceCategoryVo>> getResourceCategoryList(@RequestBody ResourceCategoryQueryPage queryPage) {
        return CommonResult.success(resourceCategoryService.getResourceCategoryList(queryPage));
    }

    /**
     * 获取全部资源分类列表
     *
     * @return
     */
    @ApiOperation("获取全部资源分类列表")
    @GetMapping("/list/all")
    public CommonResult<List<ResourceCategoryVo>> getAllResourceCategoryList() {
        return CommonResult.success(resourceCategoryService.getAllResourceCategoryList());
    }
}