package org.example.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.user.api.MenuQueryPage;
import org.example.user.entity.vo.MenuVO;

public interface MenuService {
    Boolean addMenu(MenuVO menuVo);

    Page<MenuVO> getMenuList(MenuQueryPage queryPage);

    Boolean deleteMenu(String id);

    Boolean updateMenu(MenuVO menuVo);
}