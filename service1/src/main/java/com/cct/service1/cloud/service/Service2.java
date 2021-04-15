package com.cct.service1.cloud.service;

import com.cct.service1.cloud.config.FeginConfiguration;
import com.cct.service1.cloud.model.IdVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "cct-service2",configuration = FeginConfiguration.class)
public interface Service2 {

    @PostMapping("/test/test")
    void test(@RequestBody IdVo id);
    @PostMapping("/test/test1")
    void test1(@RequestBody IdVo id);
}
