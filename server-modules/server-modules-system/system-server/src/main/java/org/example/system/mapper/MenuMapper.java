package org.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.system.entity.Menu;
import org.example.system.entity.query.MenuQueryPage;

import java.util.List;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenuList(IPage<Menu> page, @Param("queryPage") MenuQueryPage queryPage);
}