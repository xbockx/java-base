package com.linbo.spring.dao.impl;

import com.linbo.spring.dao.HelloDao;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author xbockx
 * @Date 12/15/2021
 */
public class HelloDaoImpl implements HelloDao {
    @Override
    public List<String> findAll() {
        return Arrays.asList("Hello", "World", "!");
    }
}
