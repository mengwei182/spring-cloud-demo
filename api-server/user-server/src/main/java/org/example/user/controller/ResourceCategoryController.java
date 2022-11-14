package org.example.user.controller;

import org.example.common.model.CommonResult;
import org.example.user.entity.vo.ResourceCategoryVo;
import org.example.user.service.ResourceCategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/resourceCategory")
public class ResourceCategoryController {
    @Resource
    private ResourceCategoryService resourceCategoryService;

    @RequestMapping("/add")
    public CommonResult addResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.addResourceCategory(resourceCategoryVo));
    }

    @RequestMapping("/update")
    public CommonResult updateResourceCategory(@RequestBody ResourceCategoryVo resourceCategoryVo) {
        return CommonResult.success(resourceCategoryService.updateResourceCategory(resourceCategoryVo));
    }
}