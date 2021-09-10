package dynamicproxy.handler;

import dynamicproxy.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserServiceHandler implements InvocationHandler {

    private UserService targetService;

    public UserServiceHandler(UserService userService) {
        this.targetService = userService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object res = method.invoke(targetService, args);
        System.out.println("代理: 打印日志...");
        return res;
    }
}
