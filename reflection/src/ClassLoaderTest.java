import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClassLoaderTest {

    @Test
    public void test1() {
        // 对于自定义类，使用系统类加载器加载
        ClassLoader classLoader1 = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader1);

        // 调用系统类加载器的getParent获取扩展类加载器
        ClassLoader classLoader2 = classLoader1.getParent();
        System.out.println(classLoader2);

        // 调用扩展类加载器的getParent方法 无法获取引导类加载器
        // 引导类加载器主要负责加载java核心类库，无法加载自定义类
        ClassLoader classLoader3 = classLoader2.getParent();
        System.out.println(classLoader3);

        ClassLoader classLoader = String.class.getClassLoader();
        System.out.println(classLoader);
    }

    @Test
    public void test2() throws IOException {

        Properties properties = new Properties();
        // 读取配置文件方式1
        // 配置文件默认为在当前的module下
//        properties.load(new FileInputStream("jdbc.properties"));

        // 读取配置文件方式2： 使用ClassLoader
        // 配置文件默认识别为 module的src下
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("jdbc2.properties");
        properties.load(resourceAsStream);

        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        System.out.println("username: " + username + "\t password: " + password);

    }

}
