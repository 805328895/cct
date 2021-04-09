package com.cct.rpc.server;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class CctRpcService implements Runnable{

    private Socket client;
    private Map<String,Object> services;

    /**
     * @param client 客户端
     * @param services 所有服务
     */
    public CctRpcService(Socket client, Map<String, Object> services) {
        super();
        this.client = client;
        this.services = services;
    }



    public void run() {
        InputStream in = null;
        ObjectInputStream oin = null;
        OutputStream out = null;
        ObjectOutputStream oout = null;
        CctRpcResponse response = new CctRpcResponse();
        try {
            // 1. 获取流
            in = client.getInputStream();
            oin = new ObjectInputStream(in);
            out = client.getOutputStream();
            oout = new ObjectOutputStream(out);

            // 2. 获取请求数据，强转参数类型
            Object param = oin.readObject();
            CctRpcRequest  request = null;
            if(!(param instanceof CctRpcRequest)){
                response.setError(new Exception("参数错误"));
                oout.writeObject(response);
                oout.flush();
                return;
            }else{
                request = (CctRpcRequest) param;
            }

            // 3. 查找并执行服务方法
            Object service = services.get(request.getClassName());
            Class<?> clazz= service.getClass();
            Method method = clazz.getMethod(request.getMethodName(), request.getParamTypes());
            Object result = method.invoke(service, request.getParams());
            // 4. 得到结果并返回
            response.setResult(result);
            oout.writeObject(response);
            oout.flush();
            return;
        } catch (Exception e) {
            try {	//异常处理
                if(oout != null){
                    response.setError(e);
                    oout.writeObject(response);
                    oout.flush();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return;
        }finally{
            try {	// 关闭流
                if(in != null) in.close();
                if(oin != null) oin.close();
                if(out != null) out.close();
                if(oout != null) oout.close();
                if(client != null) client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

