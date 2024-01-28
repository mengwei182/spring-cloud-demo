package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.model.CommonResult;
import org.example.system.api.ResourceQueryPage;
import org.example.system.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Api(tags = "资源管理")
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
    @ApiOperation("新增资源")
    @PostMapping("/add")
    public CommonResult<Boolean> addResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.addResource(resourceVo));
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @ApiOperation("删除资源")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteResource(@RequestParam String id) {
        return CommonResult.success(resourceService.deleteResource(id));
    }

    /**
     * 更新资源
     *
     * @param resourceVo
     * @return
     */
    @ApiOperation("更新资源")
    @PutMapping("/update")
    public CommonResult<Boolean> updateResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.updateResource(resourceVo));
    }

    /**
     * 分页获取资源列表
     *
     * @param queryPage
     * @return
     */
    @ApiOperation("分页获取资源列表")
    @GetMapping("/list")
    public CommonResult<Page<ResourceVo>> getResourceList(@ModelAttribute ResourceQueryPage queryPage) {
        return CommonResult.success(resourceService.getResourceList(queryPage));
    }

    /**
     * 获取全部资源列表
     *
     * @return
     */
    @ApiOperation("获取全部资源列表")
    @GetMapping("/list/all")
    public CommonResult<List<ResourceVo>> getAllResourceList() {
        return CommonResult.success(resourceService.getAllResourceList());
    }

    /**
     * 根据id获取资源详情
     *
     * @return
     */
    @ApiOperation("根据id获取资源详情")
    @GetMapping("/{id}")
    public CommonResult<ResourceVo> getResourceById(@PathVariable String id) {
        return CommonResult.success(resourceService.getResourceById(id));
    }

    /**
     * 刷新系统中所有资源
     *
     * @return
     */
    @ApiOperation("刷新系统中所有资源")
    @GetMapping("/refresh")
    public CommonResult<?> refreshResource() {
        resourceService.refreshResource();
        return CommonResult.success();
    }
}