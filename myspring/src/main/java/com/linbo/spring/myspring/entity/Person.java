package com.linbo.spring.myspring.entity;

import com.linbo.spring.myspring.annotation.Autowired;
import com.linbo.spring.myspring.annotation.Component;
import com.linbo.spring.myspring.annotation.Qualifier;
import com.linbo.spring.myspring.annotation.Value;
import lombok.Data;

@Data
@Component
public class Person {
    @Value("zs")
    private String name;
    @Value("18")
    private Integer age;
    @Autowired
    private Address address;
}
