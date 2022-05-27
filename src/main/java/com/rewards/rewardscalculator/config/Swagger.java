package com.rewards.rewardscalculator.config;
/*
 *Author : Sagar Enagaluru
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.rewards.rewardscalculator.controller")
public class Swagger {
    @Value("${swagger.submit.methods}")
    private String[] submitMethods;

    @Bean
    public Docket generate() {
        return new Docket(DocumentationType.SPRING_WEB)
                .apiInfo(apiInfo()).select()
                .paths(PathSelectors.regex("/customers.*")).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Rewards Calculation Documentation")
                .description("API's for Rewards Calculator")
                .version("1.0")
                .build();
    }
}
