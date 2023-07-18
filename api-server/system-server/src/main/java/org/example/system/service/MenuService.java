package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.system.vo.MenuVo;
import org.example.system.api.MenuQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
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
    List<MenuVo> getMenuTreeList();
}