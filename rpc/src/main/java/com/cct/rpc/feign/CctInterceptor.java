package com.cct.rpc.feign;

import com.cct.rpc.client.CctRpcClientProxy;
import com.cct.rpc.local.CctTransactionModel;
import com.cct.rpc.local.CctTransactionalFactory;
import com.cct.rpc.service.TranactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Configuration
public class CctInterceptor implements HandlerInterceptor {

    @Value("${cct.server.host}")
    private String cctHost;
    @Value("${cct.server.port}")
    private Integer cctPort;
    @Value("${cct.server.no}")
    private Integer cctNo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String transactionId = request.getHeader("cct-transaction");
        if(!StringUtils.isEmpty(transactionId)){
            CctTransactionModel m = new CctTransactionModel();
            m.setNo(cctNo);
            CctRpcClientProxy proxy = new CctRpcClientProxy(cctHost,cctPort);
            TranactionService  service = (TranactionService) proxy.getProxy(TranactionService.class);
            m.setService(service);
            m.setTransactionId(transactionId);
            CctTransactionalFactory.setTransactional(m);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // TODO Auto-generated method stub
        String e="";

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CctTransactionalFactory.remove();
    }
}
