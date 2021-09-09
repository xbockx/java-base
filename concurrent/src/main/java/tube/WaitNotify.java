package tube;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.WaitNotify")
public class WaitNotify {
    static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                log.debug("running...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("pool");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                log.debug("running...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("other...");
            }
        }, "t2").start();

        Thread.sleep(2000);
        log.debug("notify...");
        synchronized (lock) {
            // 唤醒WaitSet中一个线程
            //lock.notify();
            // 唤醒WaitSet中所有线程
            lock.notifyAll();
        }
    }
}
