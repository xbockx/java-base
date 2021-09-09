package tube;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.SleepWait")
public class SleepWait {
    static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (lock) {
                log.debug("获得锁");
                try {
                    //Thread.sleep(20000);
                    lock.wait(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        Thread.sleep(1000);
        synchronized (lock) {
            log.debug("获得锁");
        }
    }
}
