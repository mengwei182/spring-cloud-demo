package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.system.api.MenuQueryPage;
import org.example.system.entity.Menu;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenuList(IPage<Menu> page, @Param("queryPage") MenuQueryPage queryPage);

    List<Menu> getMenusByUserId(String userId);
}