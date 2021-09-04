import com.sun.javaws.IconUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionTest {

    @Test
    public void test1() throws Exception {
        // 反射创建对象
        Class clazz = Person.class;
        Constructor constructor = clazz.getConstructor(String.class, int.class);
        Object obj = constructor.newInstance("Tom", 12);
        Person p = (Person) obj;
        System.out.println(p);

        // 反射调用属性
        Field age = clazz.getDeclaredField("age");
        age.set(p, 10);
        System.out.println(p);

        // 反射调用方法
        Method show = clazz.getDeclaredMethod("show");
        show.invoke(p);
    }

    // 反射调用类的私有结构
    @Test
    public void test2() throws Exception {
        Class clazz = Person.class;
        Constructor constructor = clazz.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Person p = (Person) constructor.newInstance("Jerry");
        System.out.println(p);

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(p, "Jackson");
        System.out.println(p);

        Method showNation = clazz.getDeclaredMethod("showNation", String.class);
        showNation.setAccessible(true);
        String nation = (String) showNation.invoke(p, "China");
        System.out.println(nation);
    }

    // 获取class 实例的方式
    @Test
    public void test3() throws ClassNotFoundException {
        // 通过运行时类的class属性
        Class<Person> clazz1 = Person.class;
        System.out.println(clazz1);

        // 通过运行时类的对象，调用getClass方法
        Person p1 = new Person();
        Class clazz2 = p1.getClass();
        System.out.println(clazz2);

        // 通过Class的静态方法
        Class clazz3 = Class.forName("Person");
        System.out.println(clazz3);

        System.out.println(clazz1 == clazz2);
        System.out.println(clazz1 == clazz3);

        // 使用类的加载器
        ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        Class<?> clazz4 = classLoader.loadClass("Person");
        System.out.println(clazz4);

        System.out.println(clazz1 == clazz4);
    }

    @Test
    public void test4() {
        Class<Object> c1 = Object.class;
        Class<Comparable> c2 = Comparable.class;
        Class<String[]> c3 = String[].class;
        Class<int[][]> c4 = int[][].class;
        Class<ElementType> c5 = ElementType.class;
        Class<Override> c6 = Override.class;
        Class<Integer> c7 = int.class;
        Class<Void> c8 = void.class;
        Class<Class> c9 = Class.class;

        int[] a = new int[10];
        int[] b = new int[100];
        Class<? extends int[]> c10 = a.getClass();
        Class<? extends int[]> c11 = b.getClass();

        // 只要元素类型和维度一直，就是同一个class
        System.out.println(c10 == c11);
    }


}
