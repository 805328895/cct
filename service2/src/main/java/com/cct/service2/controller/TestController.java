package com.cct.service2.controller;

import com.alibaba.fastjson.JSON;
import com.cct.service2.dao.UserMapper;
import com.cct.service2.model.TestUser;
import com.cct.service2.vo.IdVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TestController {


    @Resource
    UserMapper userMapper;

    @PostMapping("/test/test")
    public void test(@RequestBody IdVo vo){
        log.info(""+vo.getId());
        TestUser testUser = userMapper.selectById(vo.getId());
        log.info(JSON.toJSONString(testUser));
        if(testUser == null){
            testUser = new TestUser();
            testUser.setId(vo.getId());
            testUser.setName("abd");
            userMapper.insert(testUser);
        }

    }
}
