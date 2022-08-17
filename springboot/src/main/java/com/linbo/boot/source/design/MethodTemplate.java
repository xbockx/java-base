package com.linbo.boot.source.design;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author xbockx
 * @Date 8/16/2022
 */
@Slf4j
public class MethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addBeanPostProcessor(bean -> log.info("@Autowired: {}", bean));
        beanFactory.addBeanPostProcessor(bean -> log.info("@Value: {}", bean));
        beanFactory.getBean();
    }

    @Slf4j
    static class MyBeanFactory {

        // Method Template
        public Object getBean() {
            Object bean = new Object();
            log.info("构造: {}", bean);
            log.info("依赖注入: {}", bean);
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }
            log.info("初始化: {}", bean);
            return bean;
        }

        public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
            processors.add(beanPostProcessor);
        }

        List<BeanPostProcessor> processors = new ArrayList<>();
    }

    interface BeanPostProcessor {
        void inject(Object bean);
    }

}
