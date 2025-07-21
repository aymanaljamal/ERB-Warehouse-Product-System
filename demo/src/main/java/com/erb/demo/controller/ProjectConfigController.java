package com.erb.demo.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ProjectConfigController {
        @Value("${spring.application.name}")
        private String appName;

        @Value("${spring.datasource.url}")
        private String datasourceUrl;

        @Value("${spring.datasource.username}")
        private String datasourceUsername;

        @Value("${spring.datasource.password}")
        private String datasourcePassword;

        @Value("${spring.datasource.driver-class-name}")
        private String datasourceDriver;

        @Value("${spring.jpa.hibernate.ddl-auto}")
        private String jpaDdlAuto;

        @Value("${spring.jpa.show-sql}")
        private String jpaShowSql;

        @Value("${spring.jpa.properties.hibernate.format_sql}")
        private String jpaFormatSql;

        @Value("${spring.jpa.database-platform}")
        private String jpaDialect;

        @Value("${logging.level.root}")
        private String loggingRoot;

        @Value("${logging.level.com.erb.demo}")
        private String loggingErbDemo;

        @Value("${logging.level.org.springframework.security}")
        private String loggingSecurity;

        

        @GetMapping()
        public Map<String, String> getConfig() {
            Map<String, String> configMap = new HashMap<>();
            configMap.put("spring.application.name", appName);
            configMap.put("spring.datasource.url", datasourceUrl);
            configMap.put("spring.datasource.username", datasourceUsername);
            configMap.put("spring.datasource.password", datasourcePassword);
            configMap.put("spring.datasource.driver-class-name", datasourceDriver);
            configMap.put("spring.jpa.hibernate.ddl-auto", jpaDdlAuto);
            configMap.put("spring.jpa.show-sql", jpaShowSql);
            configMap.put("spring.jpa.properties.hibernate.format_sql", jpaFormatSql);
            configMap.put("spring.jpa.database-platform", jpaDialect);
            configMap.put("logging.level.root", loggingRoot);
            configMap.put("logging.level.com.erb.demo", loggingErbDemo);
            configMap.put("logging.level.org.springframework.security", loggingSecurity);

            return configMap;
        }
    }
