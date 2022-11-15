package org.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.user.api.MenuQueryPage;
import org.example.user.entity.Menu;
import org.example.user.entity.RoleMenuRelation;
import org.example.user.entity.vo.MenuVO;
import org.example.user.mapper.MenuMapper;
import org.example.user.mapper.RoleMenuRelationMapper;
import org.example.user.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public Boolean addMenu(MenuVO menuVo) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVo, menu);
        menu.setId(CommonUtils.uuid());
        menuMapper.insert(menu);
        return true;
    }

    @Override
    public Page<MenuVO> getMenuList(MenuQueryPage queryPage) {
        Page<Menu> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<Menu> menuList = menuMapper.getMenuList(page, queryPage);
        page.setRecords(menuList);
        return PageUtils.wrap(page, MenuVO.class);
    }

    @Override
    @Transactional
    public Boolean deleteMenu(String id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        menuMapper.deleteById(id);
        QueryWrapper<RoleMenuRelation> roleMenuRelationQueryWrapper = new QueryWrapper<>();
        roleMenuRelationQueryWrapper.lambda().eq(RoleMenuRelation::getMenuId, id);
        roleMenuRelationMapper.delete(roleMenuRelationQueryWrapper);
        return true;
    }

    @Override
    public Boolean updateMenu(MenuVO menuVo) {
        Menu menu = menuMapper.selectById(menuVo.getId());
        if (menu == null) {
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        BeanUtils.copyProperties(menuVo, menu);
        menuMapper.updateById(menu);
        return true;
    }
}