package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.vo.TreeModel;
import org.example.user.api.MenuQueryPage;
import org.example.user.entity.vo.MenuVo;

import java.util.List;

public interface MenuService {
    /**
     * 新增菜单
     *
     * @param menuVo
     * @return
     */
    Boolean addMenu(MenuVo menuVo);

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    Boolean deleteMenu(String id);

    /**
     * 更新菜单
     *
     * @param menuVo
     * @return
     */
    Boolean updateMenu(MenuVo menuVo);

    /**
     * 查询菜单列表
     *
     * @param queryPage
     * @return
     */
    Page<MenuVo> getMenuList(MenuQueryPage queryPage);

    /**
     * 查询所有菜单列表
     *
     * @return
     */
    List<MenuVo> getAllMenuList();

    /**
     * 查询菜单树列表
     *
     * @return
     */
    List<TreeModel> getMenuTreeList();
}