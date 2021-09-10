package dynamicproxy.service.impl;

import dynamicproxy.service.UserService;

import java.util.Random;

public class UserServiceImpl implements UserService {
    @Override
    public int selectUserCount() {
        int i = new Random().nextInt(100);
        return i;
    }

    @Override
    public void update() {
        System.out.println("更新用户信息....");
    }

    @Override
    public void delete() {
        System.out.println("删除用户信息....");
    }
}
