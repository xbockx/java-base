package com.linbo.spring.myspring.entity;

import com.linbo.spring.myspring.annotation.Component;
import com.linbo.spring.myspring.annotation.Value;
import lombok.Data;

@Data
@Component
public class Address {
    @Value("1")
    private Integer id;
    @Value("18888888888")
    private String phone;
}
