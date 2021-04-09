package com.cct.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"com.cct"})
@SpringBootApplication
public class ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
