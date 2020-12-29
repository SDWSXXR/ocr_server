/**
 * Copyright © 2018-2021 泰山信息科技有限公司保留所有权利
 */
package com.tsit.ocr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *@Description 跨域设置
 *@author xxr
 *@date 2020-12-29 14:46:46
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
 
    @Override  
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  
                .allowedOrigins("*")  
                .allowCredentials(true)  
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);  
    }  
 
}
