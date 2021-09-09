package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.State")
public class State {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
        }, "t1");

        Thread t2 = new Thread(() -> {
            while(true) {}
        }, "t2");

        Thread t3 = new Thread(() -> {
            log.debug("running...");
        }, "t3");

        Thread t4 = new Thread(() -> {
            synchronized (State.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");

        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");

        Thread t6 = new Thread(()->{
            synchronized (State.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t6");

        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        log.debug("t1 state: {}", t1.getState());  // NEW
        log.debug("t2 state: {}", t2.getState());  // RUNNABLE
        log.debug("t3 state: {}", t3.getState());  // TERMINATED
        log.debug("t4 state: {}", t4.getState());  // TIMED_WAITING
        log.debug("t5 state: {}", t5.getState());  // WAITING
        log.debug("t6 state: {}", t6.getState());  // BLOCKED

    }
}
