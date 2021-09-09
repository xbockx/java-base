package tube;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.OrderPrint")
public class OrderPrint {

    static final Object lock = new Object();
    static boolean flag = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock) {
                while(!flag) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                flag = true;
                lock.notify();
            }
        }, "t2").start();
    }

}
