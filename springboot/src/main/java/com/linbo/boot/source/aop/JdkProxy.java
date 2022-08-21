package com.linbo.boot.source.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description
 * @Author xbockx
 * @Date 8/19/2022
 */
public class JdkProxy {

    interface Foo {
        void foo();
    }

    static class MyFoo implements Foo {
        @Override
        public void foo() {
            System.out.println("executed: myFoo()");
        }
    }

    public static void main(String[] args) {
        MyFoo myFoo = new MyFoo();
        final Foo instance = (Foo) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class[]{Foo.class}, (proxy, method, args1) -> {
            System.out.println("before: " + method.getName());
            final Object invoke = method.invoke(myFoo, args1);
            System.out.println("after: " + method.getName());
            return invoke;
        });

        instance.foo();
    }

}
