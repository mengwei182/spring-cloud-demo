package org.example.user.controller;

import org.example.common.model.CommonResult;
import org.example.user.api.ResourceQueryPage;
import org.example.user.entity.vo.ResourceVO;
import org.example.user.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    @RequestMapping("/add")
    public CommonResult addResource(@RequestBody ResourceVO resourceVo) {
        return CommonResult.success(resourceService.addResource(resourceVo));
    }

    @RequestMapping("/delete")
    public CommonResult deleteResource(@RequestParam String id) {
        return CommonResult.success(resourceService.deleteResource(id));
    }

    @RequestMapping("/update")
    public CommonResult updateResource(@RequestBody ResourceVO resourceVo) {
        return CommonResult.success(resourceService.updateResource(resourceVo));
    }

    @RequestMapping("/list")
    public CommonResult getResourceList(@ModelAttribute ResourceQueryPage queryPage) {
        return CommonResult.success(resourceService.getResourceList(queryPage));
    }
}