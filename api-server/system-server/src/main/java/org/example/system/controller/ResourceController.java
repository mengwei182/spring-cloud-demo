package org.example.system.controller;

import org.example.common.model.CommonResult;
import org.example.system.api.ResourceQueryPage;
import org.example.system.entity.vo.ResourceVo;
import org.example.system.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    @RequestMapping("/add")
    public CommonResult addResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.addResource(resourceVo));
    }

    @RequestMapping("/delete")
    public CommonResult deleteResource(@RequestParam String id) {
        return CommonResult.success(resourceService.deleteResource(id));
    }

    @RequestMapping("/update")
    public CommonResult updateResource(@RequestBody ResourceVo resourceVo) {
        return CommonResult.success(resourceService.updateResource(resourceVo));
    }

    @RequestMapping("/list")
    public CommonResult getResourceList(@ModelAttribute ResourceQueryPage queryPage) {
        return CommonResult.success(resourceService.getResourceList(queryPage));
    }
}