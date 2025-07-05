package com.tom.springboot.tx.note.infrastructure.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author Tom
 * @version 1.0.0
 * @ClassName BusiMvcConfig.java
 * @Description mvc配置
 * @createTime 2025年04月06日 10:09:00
 */
@Configuration
public class BusiMvcConfig implements WebMvcConfigurer {

    private static final String HTML_VIEW_PATTERN = "*.html";

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver(){
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewNames(HTML_VIEW_PATTERN);
        return internalResourceViewResolver;
    }
}
