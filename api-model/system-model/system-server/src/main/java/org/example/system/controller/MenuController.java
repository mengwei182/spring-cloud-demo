package org.example.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.common.model.CommonResult;
import org.example.system.query.MenuQueryPage;
import org.example.system.service.MenuService;
import org.example.system.entity.vo.MenuVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    /**
     * 新增菜单
     *
     * @param menuVO
     * @return
     */
    @ApiOperation("新增菜单")
    @PostMapping("/add")
    public CommonResult<Boolean> addMenu(@RequestBody MenuVO menuVO) {
        return CommonResult.success(menuService.addMenu(menuVO));
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @ApiOperation("删除菜单")
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteMenu(@RequestParam String id) {
        return CommonResult.success(menuService.deleteMenu(id));
    }

    /**
     * 更新菜单
     *
     * @param menuVO
     * @return
     */
    @PutMapping("更新菜单")
    @RequestMapping("/update")
    public CommonResult<Boolean> updateMenu(@RequestParam MenuVO menuVO) {
        return CommonResult.success(menuService.updateMenu(menuVO));
    }

    /**
     * 查询菜单列表
     *
     * @param queryPage
     * @return
     */
    @ApiOperation("查询菜单列表")
    @GetMapping("/list")
    public CommonResult<Page<MenuVO>> getMenuList(@ModelAttribute MenuQueryPage queryPage) {
        return CommonResult.success(menuService.getMenuList(queryPage));
    }

    /**
     * 查询所有菜单列表
     *
     * @return
     */
    @ApiOperation("查询所有菜单列表")
    @GetMapping("/list/all")
    public CommonResult<List<MenuVO>> getAllMenuList() {
        return CommonResult.success(menuService.getAllMenuList());
    }

    /**
     * 查询所有菜单树
     *
     * @return
     */
    @ApiOperation("查询所有菜单树")
    @GetMapping("/list/tree")
    public CommonResult<List<MenuVO>> getMenuTreeList() {
        return CommonResult.success(menuService.getMenuTreeList());
    }
}