package com.cct.rpc.aspect;

import com.cct.rpc.an.CctTransactional;
import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.local.CctTransactionModel;
import com.cct.rpc.local.CctTransactionalFactory;
import com.cct.rpc.service.TranactionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
@Aspect
public class CctAspect {
    @Pointcut("@annotation(com.cct.rpc.an.CctTransactional)")
    private void aspect() {
    }

    @Value("${cct.server.host}")
    private String cctHost;
    @Value("${cct.server.port}")
    private Integer cctPort;

    @Value("${cct.server.no}")
    private Integer cctNo;

    @Around("aspect()")
    public Object  around(ProceedingJoinPoint point)throws Throwable {
        Boolean isFirst = false;  //是否是第一个开启事物的
        TranactionService service =null;
        String transId =UUID.randomUUID().toString().replace("-", "");
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CctTransactional transactional = method.getAnnotation(CctTransactional.class);
            CctTransactionModel model = CctTransactionalFactory.getTranactional();
            if (model == null) {
                if (transactional != null) {
                    CctTransactionModel m = new CctTransactionModel();
                    m.setNo(cctNo);
                    CctRpcClientProxy proxy = new CctRpcClientProxy(cctHost,cctPort);
                    service = (TranactionService) proxy.getProxy(TranactionService.class);
                    m.setService(service);
                    m.setTransactionId(transId);
                    CctTransactionalFactory.setTransactional(m);
                    m.setHost(cctHost);
                    m.setPort(cctPort);
                    isFirst = true;   //第一个开启事物
                    log.info("start cct");
                }
            }
            Object result = point.proceed();
            if(isFirst){
                log.info("first cct");
                service.commit(transId);
            }
            return result;
        }catch (Exception e){
            if(isFirst) {
                if (service != null) {
                    service.rollback(transId);
                }
            }
            throw e;
        }finally {
            if(isFirst) {
                CctTransactionalFactory.remove();
            }
        }
    }
}
