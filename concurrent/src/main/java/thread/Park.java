package thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.Park")
public class Park {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark....");
            log.debug("status: {}", Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        t1.interrupt();
    }
}
