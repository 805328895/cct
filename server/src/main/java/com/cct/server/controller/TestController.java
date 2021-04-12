package com.cct.server.controller;

import com.cct.jdbc.factory.DataSourceFactory;
import com.cct.rpc.service.TranactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class TestController {

    @Resource
    DataSourceFactory dataSourceFactory;



    @Resource
    private TranactionService tranactionService;
    @GetMapping("")
    public void test() throws Exception{
        dataSourceFactory.init();
        tranactionService.select(1,"1111","select * from bb;",true);
    }
}
