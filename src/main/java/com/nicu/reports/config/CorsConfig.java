package com.nicu.reports.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/users")
            .allowedMethods("POST");
        registry.addMapping("/login")
            .allowedMethods("POST");
        registry.addMapping("/reports/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

}
