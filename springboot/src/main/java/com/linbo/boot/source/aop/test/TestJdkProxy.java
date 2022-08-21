package com.linbo.boot.source.aop.test;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author xbockx
 * @Date 8/20/2022
 */
public class TestJdkProxy {

    interface Foo {
        void foo();
        void bar();
    }

    interface InvocationHandler {
        Object invoke(Method method, Object[] args) throws Exception;
    }

    static class MyFoo implements Foo {
        @Override
        public void foo() {
            System.out.println("foo...");
        }

        @Override
        public void bar() {
            System.out.println("bar...");
        }
    }

    public static void main(String[] args) {
        Foo obj = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                System.out.println("before...");
                final Object invoke = method.invoke(new MyFoo(), args);
                System.out.println("after...");
                return invoke;
            }
        });

        obj.foo();
        obj.bar();
    }
}
