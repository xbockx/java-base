package com.linbo.boot.source.bfprocessor;

import com.linbo.boot.source.processor.Bean1;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.CachingResourceResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

//        applicationContext.registerBean(ComponentScanPostProcessor.class);

        applicationContext.registerBean(AtBeanPostProcessor.class);
        applicationContext.registerBean(MapperPostProcessor.class);

        applicationContext.refresh();

        for (String name: applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        applicationContext.close();
    }
}
