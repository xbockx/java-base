package tube;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Notify")
public class Notify {
    static final Object lock = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeOut = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (lock) {
                while(!hasCigarette) {
                    log.debug("没烟，歇会...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasCigarette) {
                    log.debug("干活！");
                } else {
                    log.debug("没干成活！");
                }
            }
        }, "小南").start();
        new Thread(() -> {
            synchronized (lock) {
                while(!hasTakeOut) {
                    log.debug("没外卖，歇会...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasTakeOut) {
                    log.debug("干活！");
                } else {
                    log.debug("没干成活！");
                }
            }
        }, "小女").start();

        Thread.sleep(1000);
        new Thread(() -> {
            synchronized (lock) {
                log.debug("外卖到了！");
                hasTakeOut = true;
                lock.notifyAll();
            }
        }, "送外卖的").start();

        Thread.sleep(500);
        new Thread(() -> {
            synchronized (lock) {
                log.debug("烟到了！");
                hasCigarette = true;
                lock.notifyAll();
            }
        }, "送烟的").start();
    }
}
