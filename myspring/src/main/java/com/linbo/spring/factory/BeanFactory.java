package com.linbo.spring.factory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description
 * @Author xbockx
 * @Date 12/15/2021
 */
public class BeanFactory {

    private static Properties properties = null;

    private static Map<String, Object> cache = new HashMap<>();

    static {
        properties = new Properties();
        try {
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("factory.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getBean() {
        final String bean = properties.getProperty("bean");
        try {
            if (cache.get(bean) == null) {
                synchronized (BeanFactory.class) {
                    if (cache.get(bean) == null) {
                        Class clazz = Class.forName(bean);
                        final Constructor constructor = clazz.getConstructor();
                        final Object o = constructor.newInstance();
                        cache.put(bean, o);
                    }
                }
            }
            return cache.get(bean);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
