package com.cct.server;

import com.cct.jdbc.factory.DataSourceFactory;
import com.cct.rpc.server.CctRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@Configuration
public class StartUp {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;
    @Autowired
    DefaultListableBeanFactory defaultListableBeanFactory;

    @Resource
    DataSourceFactory dataSourceFactory;

    @PostConstruct
    public void start(){
        dataSourceFactory.init();
        CctRpcServer server = new CctRpcServer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.start(1010, "com.cct",beanFactory,defaultListableBeanFactory);
            }
        }).start();
    }
}
