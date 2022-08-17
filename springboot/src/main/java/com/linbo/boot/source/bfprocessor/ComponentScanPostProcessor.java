package com.linbo.boot.source.bfprocessor;

import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @Description
 * @Author xbockx
 * @Date 8/17/2022
 */
public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 找到类上的 ComponentScan 注解
        final Optional<ComponentScan> annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        annotation.ifPresent(componentScan -> {
            for (String basePackage : componentScan.basePackages()) {
                // 将 basePackage 转为 路径
                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                try {
                    // 获取路径下的所有资源（文件）
                    final Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    for (Resource resource : resources) {
                        // 读取资源的元数据
                        final MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                        // 获取注解数据
                        final AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
                        if (metadata.hasAnnotation(Component.class.getName()) || metadata.hasMetaAnnotation(Component.class.getName())) {
                            // 添加 BeanDefinition
                            final AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(metadataReader.getClassMetadata().getClassName()).getBeanDefinition();
                            // 强转并注册 beanDefinition
                            if (beanFactory instanceof DefaultListableBeanFactory) {
                                DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
                                final String name = generator.generateBeanName(beanDefinition, factory);
                                factory.registerBeanDefinition(name, beanDefinition);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
