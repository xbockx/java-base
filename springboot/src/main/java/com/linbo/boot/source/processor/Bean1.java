package com.linbo.boot.source.processor;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * @Description
 * @Author xbockx
 * @Date 8/17/2022
 */
@Slf4j
public class Bean1 {

    private Bean2 bean2;

    private Bean3 bean3;

    private String home;

    @Autowired
    public void setBean2(Bean2 bean2) {
        this.bean2 = bean2;
    }

    public Bean2 getBean2() {
        return this.bean2;
    }

    @Autowired
    public void setHome(@Value("${java.home}") String home) {
        this.home = home;
    }

    @Resource
    public void setBean3(Bean3 bean3) {
        this.bean3 = bean3;
    }

    public Bean3 getBean3() {
        return this.bean3;
    }

    public void getHome() {
        log.info("home: {}", this.home);
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "bean2=" + bean2 +
                ", bean3=" + bean3 +
                ", home='" + home + '\'' +
                '}';
    }
}
