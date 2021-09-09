package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Sleep")
public class Sleep {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            try {
                log.debug("enter sleeping...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.debug("interrupted...");
                e.printStackTrace();
            }
        }, "t1");
        t1.start();

        Thread.sleep(500);
        log.debug("interrupting");
        t1.interrupt();

    }
}
