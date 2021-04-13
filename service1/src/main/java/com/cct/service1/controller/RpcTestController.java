package com.cct.service1.controller;


import com.cct.rpc.an.CctTransactional;
import com.cct.service1.cloud.model.IdVo;
import com.cct.service1.cloud.service.Service2;
import com.cct.service1.dao.UserMapper;
import com.cct.service1.model.TestUser;
import com.cct.service1.service.TestPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("test")
public class RpcTestController {

    @Resource
    UserMapper userMapper;

    @Resource
    Service2 service2;

    @Resource
    private TestPoolService testPoolService;

//    @CctTransactional
//    @Transactional
    @GetMapping("test")
    public void ttt() throws Exception{
        for( Integer i =0;i<50;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    log.info("---------------");
                    TestUser testUser = new TestUser();
                    testUser.setName("a");
                    testPoolService.insertUser(testUser);
                }
            }).start();
        }

    }
}
