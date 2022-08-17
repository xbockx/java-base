package com.linbo.boot.source.bfprocessor;

import com.linbo.boot.source.processor.Bean1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author xbockx
 * @Date 8/17/2022
 */
@Configuration
@ComponentScan(basePackages = "com.linbo.boot.source.bfprocessor.component")
public class Config {

    @Bean
    public Bean1 bean1() {
        return new Bean1();
    }

}
