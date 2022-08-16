package com.linbo.boot.bf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @Description
 * @Author xbockx
 * @Date 8/16/2022
 */
public class TestBeanFactory {
    public static void main(String[] args) {
        // beanFactory 实现类
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // bean 的定义
        final AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        // 给 BeanFactory 添加一些常用的后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        // BeanFactory 后处理器, 补充 bean 定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        // Bean 后处理器， 针对 bean 生命周期各个阶段提供扩展，如 @Autowired
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanPostProcessor -> {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });

        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        // 默认懒加载
        // 提前加载单例
        beanFactory.preInstantiateSingletons();
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
    }

    @Configuration
    static class Config {

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

    }

    @Slf4j
    static class Bean1 {
        @Autowired
        private Bean2 bean2;

        public Bean1() {
            log.info("bean1");
        }

        public Bean2 getBean2() {
            return bean2;
        }
    }

    @Slf4j
    static class Bean2 {
        public Bean2() {
            log.info("bean2");
        }
    }
}
