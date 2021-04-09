package com.cct.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value= {"classpath:source.properties", "classpath:application.properties"}
        , encoding="utf-8"
        ,ignoreResourceNotFound=true)
@SpringBootApplication
@ComponentScan(basePackages = {"com.cct"})
public class JdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdbcApplication.class, args);
    }
}
