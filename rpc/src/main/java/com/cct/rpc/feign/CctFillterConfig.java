package com.cct.rpc.feign;

import com.alibaba.fastjson.JSON;
import com.cct.rpc.local.CctTransactionModel;
import com.cct.rpc.local.CctTransactionalFactory;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @类描述
 * @注意：
 */
@Slf4j
@Configuration
@EnableWebMvc
@ComponentScan
public class CctFillterConfig extends WebMvcConfigurerAdapter  {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if(attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
                CctTransactionModel model = CctTransactionalFactory.getTranactional();
                if(model != null){
                    requestTemplate.header("cct-transaction", model.getTransactionId());
                    requestTemplate.header("cct-create", JSON.toJSONString(model.getHasCreate()));
                    requestTemplate.header("cct-host", model.getHost());
                    requestTemplate.header("cct-port", String.valueOf(model.getPort()));
                }
            }
        };
    }

    public CctFillterConfig(){
        super();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    CctInterceptor getInterceptor(){
        return new CctInterceptor();
    }

}
