package com.cct.mybatis.register;


import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


public class CctImportBeanDefinitionRegistrar implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{"com.cct.mybatis.adv.CctMyBatisInterceptor","com.cct.rpc.aspect.CctAspect"
                    ,"com.cct.rpc.feign.CctFillterConfig","com.cct.rpc.feign.FeignFillter"};
    }
}
