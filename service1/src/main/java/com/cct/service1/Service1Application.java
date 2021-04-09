package com.cct.service1;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cct"})
@MapperScan("com.cct.service1.dao")
public class Service1Application {

    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }

}
