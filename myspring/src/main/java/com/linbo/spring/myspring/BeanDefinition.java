package com.linbo.spring.myspring;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BeanDefinition {

    private String beanName;
    private Class beanClass;

}
