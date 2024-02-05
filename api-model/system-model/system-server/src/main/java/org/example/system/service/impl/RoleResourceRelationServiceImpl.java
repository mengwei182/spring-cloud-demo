package org.example.system.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.system.dubbo.RoleResourceRelationDubboService;
import org.example.system.service.RoleResourceRelationService;
import org.springframework.stereotype.Service;

/**
 * @author lihui
 * @since 2023/4/3
 */
@Service
@DubboService(interfaceClass = RoleResourceRelationDubboService.class, interfaceName = "roleResourceRelationDubboService")
public class RoleResourceRelationServiceImpl implements RoleResourceRelationService, RoleResourceRelationDubboService {
}