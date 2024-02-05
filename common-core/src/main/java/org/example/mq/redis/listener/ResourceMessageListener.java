package org.example.mq.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.CaffeineRedisCache;
import org.example.common.model.CommonResult;
import org.example.system.dubbo.ResourceCategoryDubboService;
import org.example.system.dubbo.ResourceDubboService;
import org.example.system.vo.ResourceCategoryVO;
import org.example.system.vo.ResourceVO;
import org.example.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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
    private CaffeineRedisCache caffeineRedisCache;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommonResult<?> commonResult = (CommonResult<?>) caffeineRedisCache.getRedisTemplate().getValueSerializer().deserialize(message.getBody());
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
            ResourceCategoryVO resourceCategoryVO = resourceCategoryDubboService.getResourceCategoryByName(categoryName);
            // 资源分类不存在
            if (resourceCategoryVO == null) {
                resourceCategoryVO = new ResourceCategoryVO();
                resourceCategoryVO.setId(categoryId);
                resourceCategoryVO.setName(categoryName);
                resourceCategoryVO = resourceCategoryDubboService.addResourceCategory(resourceCategoryVO);
                log.info("add resource category:{}", resourceCategoryVO.getName());
            }
            categoryId = resourceCategoryVO.getId();
            Set<PathPattern> patterns = pathPatternsCondition.getPatterns();
            for (PathPattern p : patterns) {
                ResourceVO resourceVO = resourceDubboService.getResource(p.getPatternString(), categoryId);
                // 资源已存在，直接跳过
                if (resourceVO != null) {
                    continue;
                }
                resourceVO = new ResourceVO();
                resourceVO.setName(handlerMethod.getMethod().getName());
                resourceVO.setCategoryId(categoryId);
                resourceVO.setUrl(p.getPatternString());
                resourceDubboService.addResource(resourceVO);
                log.info("add resource:{}", resourceVO.getName());
            }
        }
    }
}