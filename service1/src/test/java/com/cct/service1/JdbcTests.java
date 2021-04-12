package com.cct.service1;

import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.service.TranactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTests {

    @Test
    public void select() throws Exception{
        CctRpcClientProxy proxy = new CctRpcClientProxy("127.0.0.1",1010);
        TranactionService service = (TranactionService) proxy.getProxy(TranactionService.class);
        List abcd = service.select(1,"1111","select * from bb;",true);
        System.out.println(abcd);
    }
}
