package com.linbo.boot.source.bfprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

/**
 * @Description
 * @Author xbockx
 * @Date 8/18/2022
 */
public class AtBeanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        for (String basePackage : annotation.basePackages()) {
            String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
            try {
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                for (Resource resource : resources) {
                    final MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                    final AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    final Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(Bean.class.getName());
                    for (MethodMetadata method : annotatedMethods) {
                        final BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
                        beanDefinitionBuilder.setFactoryMethodOnBean(method.getMethodName(), "config"); //TODO
//                        beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                        final AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                        if (beanFactory instanceof DefaultListableBeanFactory) {
                            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
                            factory.registerBeanDefinition(method.getMethodName(), beanDefinition);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
