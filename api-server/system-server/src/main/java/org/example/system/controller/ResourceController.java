package org.example.system.controller;

import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.model.CommonResult;
import org.example.system.api.ResourceQueryPage;
import org.example.system.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lihui
 * @since 2023/4/3
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    /**
     * 新增资源
     *
     * @param resourceVo
     * @return
     */
    @RequestMapping("/add")
    public CommonResult addResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.addResource(resourceVo));
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public CommonResult deleteResource(@RequestParam String id) {
        return CommonResult.success(resourceService.deleteResource(id));
    }

    /**
     * 更新资源
     *
     * @param resourceVo
     * @return
     */
    @RequestMapping("/update")
    public CommonResult updateResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.updateResource(resourceVo));
    }

    /**
     * 分页获取资源列表
     *
     * @param queryPage
     * @return
     */
    @RequestMapping("/list")
    public CommonResult getResourceList(@ModelAttribute ResourceQueryPage queryPage) {
        return CommonResult.success(resourceService.getResourceList(queryPage));
    }

    /**
     * 获取全部资源列表
     *
     * @return
     */
    @RequestMapping("/list/all")
    public CommonResult getAllResourceList() {
        return CommonResult.success(resourceService.getAllResourceList());
    }

    /**
     * 根据id获取资源详情
     *
     * @return
     */
    @RequestMapping("/{id}")
    public CommonResult getResourceById(@PathVariable String id) {
        return CommonResult.success(resourceService.getResourceById(id));
    }
}