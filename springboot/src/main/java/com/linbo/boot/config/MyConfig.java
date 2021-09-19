package com.linbo.boot.config;

import com.linbo.boot.bean.Pet;
import com.linbo.boot.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1、配置类里面使用费@Bean注解标注在方法上给容器注册组件， 默认单例
 * 2、配置类也是组件
 * 3、proxyBeanMethods 代理bean的方法
 *      Full配置（proxyBeanMethods = true）
 *      Lite配置（proxyBeanMethods = false）
 *      解决组件依赖
 *
 *      配置类中的组件无依赖使用lite配置加速启动过程， 减少判断
 *      配置类组件有依赖关系，方法会被调用得到之前的单例组件， 使用full配置
 *
 */
@Configuration(proxyBeanMethods = true)  // 这是一个配置类 相当于beans.xml等配置文件
public class MyConfig {

    // 向容器中添加组件
    @Bean("myUser")
    public User user02() {
        User user = new User("ls", 20);
        // user组件依赖pet组件
        user.setPet(tomcat());
        return user;
    }

    @Bean("myTom")
    public Pet tomcat() {
        return new Pet("tomcat");
    }

}
