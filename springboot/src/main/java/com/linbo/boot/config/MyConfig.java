package com.linbo.boot.config;

import ch.qos.logback.core.db.DBHelper;
import com.linbo.boot.bean.Car;
import com.linbo.boot.bean.Pet;
import com.linbo.boot.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

/**
 * 1、配置类里面使用费@Bean注解标注在方法上给容器注册组件， 默认单例
 * 2、配置类也是组件
 * 3、proxyBeanMethods 代理bean的方法
 * Full配置（proxyBeanMethods = true）
 * Lite配置（proxyBeanMethods = false）
 * 解决组件依赖
 * <p>
 * 配置类中的组件无依赖使用lite配置加速启动过程， 减少判断
 * 配置类组件有依赖关系，方法会被调用得到之前的单例组件， 使用full配置
 * <p>
 * 4、@Import({User.class, DBHelper.class})
 * 给容器自动创建出这两个类型的组件，默认组件名是全类名
 */
// 1、开启Car配置绑定功能
// 2、将组件自动注册到容器中
@EnableConfigurationProperties(Car.class)
@ImportResource("classpath:beans.xml")
@Import({User.class, DBHelper.class})
@Configuration(proxyBeanMethods = true)  // 这是一个配置类 相当于beans.xml等配置文件
public class MyConfig {

    // 向容器中添加组件
    @ConditionalOnBean(name = "myTom")      // 当容器中有myTom组件时才注册myUser组件
    @Bean("myUser")
    public User user02() {
        User user = new User("ls", 20);
        // user组件依赖pet组件
        user.setPet(tomcat());
        return user;
    }

    //    @Bean("myTom")
    public Pet tomcat() {
        return new Pet("tomcat");
    }

}
