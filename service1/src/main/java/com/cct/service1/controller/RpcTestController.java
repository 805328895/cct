package com.cct.service1.controller;


import com.cct.rpc.an.CctTransactional;
import com.cct.service1.cloud.model.IdVo;
import com.cct.service1.cloud.service.Service2;
import com.cct.service1.dao.UserMapper;
import com.cct.service1.model.TestUser;
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

    @CctTransactional
//    @Transactional
    @GetMapping("test")
    public void ttt() throws Exception{
        TestUser testUser = new TestUser();
        testUser.setName("a");
        Integer i = userMapper.insert(testUser);
        log.info("id:"+testUser.getId());
        IdVo idVo = new IdVo();
        idVo.setId(testUser.getId());
        service2.test(idVo);
    }
}
