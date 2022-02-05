package dynamicproxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CustomHandler implements InvocationHandler {

    private Object target;

    public CustomHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        float price = (float) method.invoke(target, args);
        price += 100;
        System.out.println("赠: 碎屏险 * 1");

        return price;
    }
}
