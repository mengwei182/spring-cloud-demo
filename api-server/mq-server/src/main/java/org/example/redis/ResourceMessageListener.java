package org.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.common.entity.system.vo.ResourceCategoryVo;
import org.example.common.entity.system.vo.ResourceVo;
import org.example.common.model.CommonResult;
import org.example.common.util.CommonUtils;
import org.example.dubbo.system.ResourceCategoryDubboService;
import org.example.dubbo.system.ResourceDubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * @author lihui
 * @since 2023/4/25
 */
@Slf4j
@Component
public class ResourceMessageListener implements MessageListener {
    @Value("${spring.application.name}")
    private String applicationName;
    @DubboReference
    private ResourceDubboService resourceDubboService;
    @DubboReference
    private ResourceCategoryDubboService resourceCategoryDubboService;
    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommonResult<?> commonResult = (CommonResult<?>) redisTemplate.getValueSerializer().deserialize(message.getBody());
        if (commonResult == null || commonResult.getData() == null || !Boolean.TRUE.equals(commonResult.getData())) {
            return;
        }
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
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
            String categoryName = applicationName + "::" + name;
            String categoryId = CommonUtils.uuid();
            ResourceCategoryVo resourceCategoryVo = resourceCategoryDubboService.getResourceCategoryByName(categoryName);
            // 资源分类不存在
            if (resourceCategoryVo == null) {
                resourceCategoryVo = new ResourceCategoryVo();
                resourceCategoryVo.setId(categoryId);
                resourceCategoryVo.setName(categoryName);
                resourceCategoryVo = resourceCategoryDubboService.addResourceCategory(resourceCategoryVo);
                log.info("add resource category:{}", resourceCategoryVo.getName());
            }
            categoryId = resourceCategoryVo.getId();
            Set<PathPattern> patterns = pathPatternsCondition.getPatterns();
            for (PathPattern p : patterns) {
                ResourceVo resourceVo = resourceDubboService.getResource(p.getPatternString(), categoryId);
                // 资源已存在，直接跳过
                if (resourceVo != null) {
                    continue;
                }
                resourceVo = new ResourceVo();
                resourceVo.setName(handlerMethod.getMethod().getName());
                resourceVo.setCategoryId(categoryId);
                resourceVo.setUrl(p.getPatternString());
                resourceDubboService.addResource(resourceVo);
                log.info("add resource:{}", resourceVo.getName());
            }
        }
    }
}