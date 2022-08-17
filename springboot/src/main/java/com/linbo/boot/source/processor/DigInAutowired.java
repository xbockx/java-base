package com.linbo.boot.source.processor;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author xbockx
 * @Date 8/17/2022
 */
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        // 定义 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); // @Value 解析
        beanFactory.addEmbeddedValueResolver(s -> new StandardEnvironment().resolvePlaceholders(s));    // ${} 解析

        //  查找哪些属性添加了 @Autowired
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        processor.postProcessProperties(null, bean1, "bean1");
//        System.out.println(bean1);
        final Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        final InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);

        // 注入
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        // 按类型查找值
        final Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor descriptor1 = new DependencyDescriptor(bean3, true);
        final Object o = beanFactory.doResolveDependency(descriptor1, null, null, null);
        System.out.println(o);

        final Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor descriptor2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), true);
        final Object o1 = beanFactory.doResolveDependency(descriptor2, null, null, null);
        System.out.println(o1);
    }
}
