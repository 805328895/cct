package com.cct.service1.service.impl;

import com.cct.rpc.an.CctTransactional;
import com.cct.service1.cloud.model.IdVo;
import com.cct.service1.cloud.service.Service2;
import com.cct.service1.dao.UserMapper;
import com.cct.service1.model.TestUser;
import com.cct.service1.service.TestPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TestPoolServiceImpl implements TestPoolService {
    @Resource
    private Service2 service2;
    @Resource
    private UserMapper userMapper;
//    @CctTransactional
    @Override
    public void insertUser(TestUser testUser) {
        Integer i = userMapper.insert(testUser);
        log.info("id:" + testUser.getId());
        IdVo idVo = new IdVo();
        idVo.setId(testUser.getId());

        service2.test(idVo);

    }
}
