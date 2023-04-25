package org.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.common.configuration.ApplicationConfiguration;
import org.example.common.entity.system.Resource;
import org.example.common.entity.system.ResourceCategory;
import org.example.common.util.CommonUtils;
import org.example.dubbo.system.ResourceCategoryDubboService;
import org.example.dubbo.system.ResourceDubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;

/**
 * @author lihui
 * @since 2023/4/25
 */
@Slf4j
@Component
public class ReceiverMessageReceiver {
    @Value("${spring.application.name}")
    private String applicationName;
    @DubboReference
    private ResourceDubboService resourceDubboService;
    @DubboReference
    private ResourceCategoryDubboService resourceCategoryDubboService;

    public void refreshResource(String start) {
        if (!Boolean.TRUE.equals(Boolean.parseBoolean(start))) {
            return;
        }
        ApplicationContext applicationContext = ApplicationConfiguration.getApplicationContext();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Set<RequestMappingInfo> requestMappingInfos = map.keySet();
        for (RequestMappingInfo requestMappingInfo : requestMappingInfos) {
            // controller类名称
            HandlerMethod handlerMethod = map.get(requestMappingInfo);
            // controller类全限定名
            String name = handlerMethod.getBeanType().getName();
            // 请求地址
            PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
            if (pathPatternsCondition == null) {
                continue;
            }
            // 请求方式
            Set<RequestMethod> requestMethods = requestMappingInfo.getMethodsCondition().getMethods();

            String categoryName = applicationName + "_" + name;
            String categoryId = CommonUtils.uuid();
            ResourceCategory resourceCategory = resourceCategoryDubboService.getResourceCategory(categoryName);
            if (resourceCategory != null) {
                categoryId = resourceCategory.getId();
            } else {
                resourceCategory = new ResourceCategory();
                resourceCategory.setId(categoryId);
                resourceCategory.setName(categoryName);
//                    resourceCategoryMapper.insert(resourceCategory);
            }
            Set<PathPattern> patterns = pathPatternsCondition.getPatterns();
            for (PathPattern pattern : patterns) {
                Resource resource = resourceDubboService.getResource(pattern.getPatternString(), categoryId);
                // 资源已存在，直接跳过
                if (resource != null) {
                    continue;
                }
                resource = new Resource();
                resource.setId(CommonUtils.uuid());
//                    resource.setName();
                resource.setCategoryId(categoryId);
                resource.setUrl(pattern.getPatternString());
//                    resourceMapper.insert(resource);
            }
        }
    }
}