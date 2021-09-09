package tube;

import java.util.ArrayList;
import java.util.List;

public class ThreadSafeTest {
    private static final int THREAD_NUMBER = 2;
    private static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        //ThreadUnsafe threadUnsafe = new ThreadUnsafe();
        ThreadSafe threadSafe = new ThreadSafe();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                //threadUnsafe.method1(LOOP_NUMBER);
                threadSafe.method1(LOOP_NUMBER);
            }, "Thread"+(i+1)).start();
        }
    }
}
// 成员变量
class ThreadUnsafe {
    List<Integer> list = new ArrayList<>();

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();
        }
    }

    public void method2() {
        list.add(1);
    }

    public void method3() {
        list.remove(0);
    }
}
//局部变量
class ThreadSafe {
    public void method1(int loopNumber) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    public void method2(List list) {
        list.add(1);
    }

    public void method3(List list) {
        list.remove(0);
    }
}
