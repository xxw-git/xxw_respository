package com.lc.xxw.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @description: swagger2配置信息
 *  [Springfox官方集成文档](http://springfox.github.io/springfox/docs/current/)
 *  [Swagger注解官方文档](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X)
 * @author: xuexiaowei
 * @create: 2019-07-21 08:42
 */

@Configuration
@EnableSwagger2
@EnableWebMvc   //启用Mvc，非springboot框架需要引入注解@EnableWebMvc
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                //扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Spring MVC中使用Swagger2构建RESTful APIs")
                .description("基础平台 RESTful 风格的接口文档，内容详细，极大的减少了前后端的沟通成本，同时确保代码与文档保持高度一致，极大的减少维护文档的时间。")
                .termsOfServiceUrl("https://www.jianshu.com/p/12f4394462d5")
                .version("1.0.0")
                .contact("xxw")
                .build();
    }
}
