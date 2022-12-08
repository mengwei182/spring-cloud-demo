//package org.example.gateway.configuration;
//
//import org.example.common.interceptor.UserContextInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
///**
// * @author lihui
// * @since 2022/10/26
// */
//@Configuration
//public class WebMvcConfiguration implements WebMvcConfigurer {
////    @Resource
////    private AuthorizationInterceptor authorizationInterceptor;
//    @Resource
//    private UserContextInterceptor userContextInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(authorizationInterceptor).excludePathPatterns("/error").addPathPatterns("/**");
//        registry.addInterceptor(userContextInterceptor).excludePathPatterns("/error").addPathPatterns("/**");
//    }
//}