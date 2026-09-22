package com.portfolio.raven.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class AdminUploadConfiguration {
    @Bean public MultipartConfigElement multipartConfigElement() {
        var config = new MultipartConfigFactory();
        config.setMaxFileSize(DataSize.ofMegabytes(5));
        config.setMaxRequestSize(DataSize.ofMegabytes(6));
        return config.createMultipartConfig();
    }
}
