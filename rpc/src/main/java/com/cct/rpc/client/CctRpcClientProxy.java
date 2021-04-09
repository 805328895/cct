package com.cct.rpc.client;

import com.cct.rpc.server.CctRpcRequest;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CctRpcClientProxy<T> implements InvocationHandler {
    private String host;
    private int port;

    public CctRpcClientProxy(String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * 生成代理对象
     * @param clazz 代理类型（接口）
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T>T getProxy(Class<T> clazz){
        // clazz不是接口不能使用JDK动态代理
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, CctRpcClientProxy.this);
    }

    public Object invoke(Object obj, Method method, Object[] params) throws Throwable {
        //封装参数
        CctRpcRequest request = new CctRpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(params);
        //链接服务器调用服务
        CctRpcClient client = new CctRpcClient();
        return client.start(request, host, port);
    }

}
