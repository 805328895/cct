package com.cct.service1.controller;

import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.service.TestRpc;
import com.cct.rpc.service.TranactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class RpcTestController {
    @GetMapping("test")
    public void ttt() throws Exception{
        CctRpcClientProxy proxy = new CctRpcClientProxy("127.0.0.1",1010);
        TranactionService service = (TranactionService) proxy.getProxy(TranactionService.class);

        List abcd = service.select(1,"1111","select * from bb;");
        System.out.println(abcd);
    }
}
