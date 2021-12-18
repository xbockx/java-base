package com.linbo.spring.myspring;

import com.linbo.spring.myspring.annotation.Autowired;
import com.linbo.spring.myspring.annotation.Component;
import com.linbo.spring.myspring.annotation.Qualifier;
import com.linbo.spring.myspring.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AnnotationConfigApplicationContext {

    private final Map<String, Object> container = new HashMap<>();

    public AnnotationConfigApplicationContext(String basePackage) {
        // find all class with @Component in base package and add them to beanDefinitions
        Set<BeanDefinition> beanDefinitions = findBeanDefinitions(basePackage);
        // create object and add into ioc container
        createObject(beanDefinitions);
        // complete autowired
        autowireObject(beanDefinitions);

    }

    public Object getBean(String beanName) {
        return container.get(beanName);
    }

    private void autowireObject(Set<BeanDefinition> beanDefinitions) {
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            Class beanClass = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Autowired autowiredAnnotation = declaredField.getAnnotation(Autowired.class);
                if (autowiredAnnotation != null) {
                    Qualifier qualifierAnnotation = declaredField.getAnnotation(Qualifier.class);
                    Object bean = null;
                    if (qualifierAnnotation != null) {
                        // byName
                        bean = container.get(qualifierAnnotation.value());
                    } else {
                        // byType
                        String name = declaredField.getName().substring(0, 1).toLowerCase() + declaredField.getName().substring(1);
                        bean = container.get(name);
                    }
                    try {
                        Method method = beanClass.getMethod("set" + declaredField.getName().substring(0, 1).toUpperCase() + declaredField.getName().substring(1), declaredField.getType());
                        method.invoke(container.get(beanName), bean);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void createObject(Set<BeanDefinition> beanDefinitions) {
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()) {
            BeanDefinition beanDefinition = iterator.next();
            String beanName = beanDefinition.getBeanName();
            Class beanClass = beanDefinition.getBeanClass();
            try {
                Object o = beanClass.getConstructor().newInstance();
                // add value
                Field[] declaredFields = beanClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Value valueAnnotation = declaredField.getAnnotation(Value.class);
                    if (valueAnnotation != null) {
                        Method method = beanClass.getMethod("set" + declaredField.getName().substring(0, 1).toUpperCase() + declaredField.getName().substring(1), declaredField.getType());
                        Object value = valueAnnotation.value();
                        switch (declaredField.getType().getSimpleName()) {
                            case "String":
                                break;
                            case "Integer":
                                value = Integer.valueOf(valueAnnotation.value());
                                break;
                            case "Float":
                                value = Float.valueOf(valueAnnotation.value());
                                break;
                            case "Double":
                                value = Double.valueOf(valueAnnotation.value());
                                break;
                            case "Long":
                                value = Long.valueOf(valueAnnotation.value());
                                break;
                            default:
                                throw new RuntimeException("illegal type");
                        }
                        method.invoke(o, value);
                    }
                }
                // add object to ioc container
                container.put(beanName, o);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Set<BeanDefinition> findBeanDefinitions(String basePackage) {
        Set<Class<?>> classes = MyTools.getClasses(basePackage);
        Iterator<Class<?>> iterator = classes.iterator();
        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        while (iterator.hasNext()) {
            Class<?> next = iterator.next();
            Component componentAnnotation = next.getAnnotation(Component.class);
            if (componentAnnotation != null) {
                String beanName = componentAnnotation.value();
                if (beanName.equals("")) {
                    beanName = next.getSimpleName().substring(0, 1).toLowerCase() + next.getSimpleName().substring(1);
                }
                beanDefinitions.add(new BeanDefinition(beanName, next));
            }
        }
        return beanDefinitions;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.linbo.spring.myspring.entity");
        System.out.println(annotationConfigApplicationContext.container);
    }

}
