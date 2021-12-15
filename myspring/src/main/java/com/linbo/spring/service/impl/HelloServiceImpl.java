package com.linbo.spring.service.impl;

import com.linbo.spring.dao.HelloDao;
import com.linbo.spring.dao.impl.HelloDaoImpl;
import com.linbo.spring.factory.BeanFactory;
import com.linbo.spring.service.HelloService;

import java.util.List;

/**
 * @Description
 * @Author xbockx
 * @Date 12/15/2021
 */
public class HelloServiceImpl implements HelloService {

    private HelloDao helloDao = (HelloDao) BeanFactory.getBean();

    {
        for (int i = 0; i < 10; i++) {
            helloDao = (HelloDao) BeanFactory.getBean();
            System.out.println(helloDao);
        }
    }

    @Override
    public List<String> findAll() {
        System.out.println(helloDao.toString());
        return helloDao.findAll();
    }
}
