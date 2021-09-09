package jmm;

import lombok.extern.slf4j.Slf4j;

public class TestTwoPhaseTerminal {
    public static void main(String[] args) throws InterruptedException {
         TwoPhaseTerminal tpt = new TwoPhaseTerminal();
         tpt.start();

         Thread.sleep(5000);
         tpt.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTerminal")
class TwoPhaseTerminal {

    private Thread monitor;
    private volatile boolean stop;

    public void start() {
        monitor = new Thread(() -> {
            while(true) {
                if (stop) {
                    log.debug("料理后事...");
                    break;
                }
                try {
                    log.debug("monitoring....");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        monitor.start();
    }

    public void stop() {
        stop = true;
    }

}
