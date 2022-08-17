package com.linbo.boot.source.bfprocessor;

import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.CachingResourceResolver;

import java.io.IOException;
import java.util.Optional;

/**
 * @Description
 * @Author xbockx
 * @Date 8/17/2022
 */
public class Application {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.registerBean("config", Config.class);
//        applicationContext.registerBean(ConfigurationClassPostProcessor.class);

        applicationContext.registerBean(ComponentScanPostProcessor.class);

        applicationContext.refresh();

        for (String name: applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        applicationContext.close();
    }
}
