package org.example.configuration;

import io.swagger.annotations.ApiOperation;
import org.example.common.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * @author lihui
 * @since 2024/1/26
 */
@EnableOpenApi
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public Docket openApi() {
        // apiInfo
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title(applicationName + " API");
        apiInfoBuilder.description(applicationName + " api");
        apiInfoBuilder.termsOfServiceUrl("http://localhost:" + serverPort);
        apiInfoBuilder.version("1.0");
        ApiInfo apiInfo = apiInfoBuilder.build();

        // globalRequestParameters
        RequestParameterBuilder requestParameterBuilder = new RequestParameterBuilder();
        requestParameterBuilder.name(CommonConstant.AUTHORIZATION);
        requestParameterBuilder.description("token");
        requestParameterBuilder.in(ParameterType.HEADER);
        requestParameterBuilder.required(false);
        RequestParameter requestParameter = requestParameterBuilder.build();

        // docket
        Docket docket = new Docket(DocumentationType.OAS_30);
        docket.groupName(applicationName);
        docket.apiInfo(apiInfo);
        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        select.paths(PathSelectors.any());
        docket = select.build();
        docket.globalRequestParameters(Collections.singletonList(requestParameter));
        docket.securitySchemes(getSecuritySchemes());
        docket.securityContexts(getSecurityContexts());
        docket.enable(!CommonConstant.PROFILE_PROD.equals(profile));
        return docket;
    }

    private List<SecurityScheme> getSecuritySchemes() {
        return Collections.singletonList(new ApiKey(CommonConstant.AUTHORIZATION, CommonConstant.AUTHORIZATION, ParameterType.HEADER.getIn()));
    }

    private List<SecurityContext> getSecurityContexts() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(Collections.singletonList(SecurityReference.builder()
                        .reference(CommonConstant.AUTHORIZATION)
                        .scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})
                        .build()))
                // 需要认证的请求路径
                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                .build());
    }
}