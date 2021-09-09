package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Interrupt")
public class Interrupt {

    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            while(true) {
                if(Thread.currentThread().isInterrupted()) {
                    log.debug("interrupted! break loop....");
                    break;
                }
            }
        }, "t2");
        t2.start();
        log.debug("begin");
        t2.interrupt();
    }

    public void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("sleep...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        Thread.sleep(500);
        t1.interrupt();
        Thread.sleep(500);
        log.debug("flag: {}", t1.isInterrupted());
    }

}
