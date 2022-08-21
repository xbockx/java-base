package com.linbo.boot.source.aop.test;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author xbockx
 * @Date 8/20/2022
 */
public class $Proxy0 implements TestJdkProxy.Foo {

    TestJdkProxy.InvocationHandler h;

    public $Proxy0(TestJdkProxy.InvocationHandler h) {
        this.h = h;
    }

    @Override
    public void foo() {
        try {
            final Method foo = TestJdkProxy.Foo.class.getDeclaredMethod("foo");
            h.invoke(foo, new Object[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bar() {
        try {
            final Method bar = TestJdkProxy.Foo.class.getDeclaredMethod("bar");
            h.invoke(bar, new Object[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
