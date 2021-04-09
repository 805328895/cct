package com.cct.service1;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cct.rpc.an.CctTransactional;
import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.service.TranactionService;
import com.cct.service1.dao.AuthChannelMapper;
import com.cct.service1.dao.UserMapper;
import com.cct.service1.model.AuthChannel;
import com.cct.service1.model.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTests {

    @Resource
    AuthChannelMapper authChannelMapper;

    @Resource
    UserMapper userMapper;
    @Test
    public void query(){
        CctRpcClientProxy proxy = new CctRpcClientProxy("127.0.0.1",1010);
        TranactionService service = (TranactionService) proxy.getProxy(TranactionService.class);
        try {
            QueryWrapper<AuthChannel> q = new QueryWrapper<>();
            q.eq("id", 1);
            List<AuthChannel> authChannelList = authChannelMapper.selectList(q);
            String e = "";
            service.commit("1111");

        }
        catch (Exception e){
            log.error("",e);
            service.rollback("1111");

        }
    }

    @CctTransactional
    @Test
    public void update() {

        TestUser testUser = new TestUser();
        testUser.setId(1);
        testUser.setName("a");
        Integer i = userMapper.updateById(testUser);


    }
    @Test
    public void insert(){


        CctRpcClientProxy proxy = new CctRpcClientProxy("127.0.0.1",1010);
        TranactionService service = (TranactionService) proxy.getProxy(TranactionService.class);
        try {
            TestUser u = new TestUser();
            u.setName("allain");
            Integer i = userMapper.insert(u);
            System.out.println("iiii:"+i);

            service.commit("1111");
        }catch (Exception e){
            log.error("",e);
            service.rollback("1111");
        }
    }
}
