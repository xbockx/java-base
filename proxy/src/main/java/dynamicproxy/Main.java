package dynamicproxy;

import dynamicproxy.handler.CustomHandler;
import dynamicproxy.handler.UserServiceHandler;
import dynamicproxy.service.UserService;
import dynamicproxy.service.impl.UserServiceImpl;
import staticproxy.factory.Samsung;
import staticproxy.service.SellingBusiness;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        testExample2();
    }

    static void testExample() {
        SellingBusiness business = new Samsung();
        CustomHandler handler = new CustomHandler(business);
        SellingBusiness proxy = (SellingBusiness) Proxy.newProxyInstance(business.getClass().getClassLoader(),
                business.getClass().getInterfaces(),
                handler);
        System.out.println(proxy.sell(1));
    }

    static void testExample2() {
        UserService userService = new UserServiceImpl();
        InvocationHandler handler = new UserServiceHandler(userService);
        UserService proxyService = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(),
                handler);
        System.out.println("用户数量: " + proxyService.selectUserCount());
        proxyService.update();
        proxyService.delete();
    }
}
