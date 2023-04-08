package org.example.dubbo.userserver;

import org.example.common.entity.system.vo.ResourceVo;

import java.util.List;

/**
 * @author lihui
 * @since 2022/11/11
 */
public interface ResourceDubboService {
    List<ResourceVo> getResources();
}