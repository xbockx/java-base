package thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Pattern")
public class Pattern {

    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTerminate tpt = new TwoPhaseTerminate();
        tpt.start();
        Thread.sleep(5000);
        tpt.stop();
    }

}

@Slf4j(topic = "c.Terminate")
class TwoPhaseTerminate {
    private Thread monitor;

    public void start() {
        monitor = new Thread(()->{
            while(true) {
                Thread current = Thread.currentThread();
                if(current.isInterrupted()) {
                    log.debug("do something...");
                    break;
                }
                try {
                    Thread.sleep(2000);
                    log.debug("monitoring...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //重新设置清除标记
                    current.interrupt();
                }
            }
        }, "m");
        monitor.start();
    }

    public void stop() {
        if(monitor != null) {
            monitor.interrupt();
        }
    }
}
