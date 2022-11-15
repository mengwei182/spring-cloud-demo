package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.user.api.MenuQueryPage;
import org.example.user.entity.vo.MenuVo;

public interface MenuService {
    Boolean addMenu(MenuVo menuVo);

    Page<MenuVo> getMenuList(MenuQueryPage queryPage);

    Boolean deleteMenu(String id);

    Boolean updateMenu(MenuVo menuVo);
}