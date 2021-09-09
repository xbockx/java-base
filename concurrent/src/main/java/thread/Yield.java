package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Yield")
public class Yield {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    count++;
                    log.debug("t1: {}", count);
                }
            }
        };
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    //Thread.yield();
                    count++;
                    log.debug("        t2: {}", count);
                }
            }
        };
        t1.start();
        t2.start();
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
    }

}
