package com.linbo.boot.source.aop;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author xbockx
 * @Date 8/20/2022
 */
public class CglibProxy {

    static class Foo {
        void foo() {
            System.out.println("foo...");
        }
    }

    public static void main(String[] args) {
        Foo foo = new Foo();
        Foo obj = (Foo) Enhancer.create(Foo.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before...");
                final Object invoke = methodProxy.invoke(foo, null);
                System.out.println("after...");
                return invoke;
            }
        });
        obj.foo();
    }
}
