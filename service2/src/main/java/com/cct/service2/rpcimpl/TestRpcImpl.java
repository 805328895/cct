package com.cct.service2.rpcimpl;

import com.cct.rpc.server.CctService;
import com.cct.rpc.service.TestRpc;

@CctService(TestRpc.class)
public class TestRpcImpl implements TestRpc {
    public String add(String a, String b) {
        return a+b;
    }
}
