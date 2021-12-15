package com.linbo.spring.servlet;


import com.linbo.spring.service.HelloService;
import com.linbo.spring.service.impl.HelloServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @Author xbockx
 * @Date 12/15/2021
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private HelloService helloService = new HelloServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(helloService.findAll().toString());
    }
}
