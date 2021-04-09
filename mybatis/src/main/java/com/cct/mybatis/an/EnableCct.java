package com.cct.mybatis.an;

import com.cct.mybatis.register.CctImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CctImportBeanDefinitionRegistrar.class})
public @interface EnableCct {
}
