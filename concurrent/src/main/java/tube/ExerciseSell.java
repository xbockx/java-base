package tube;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j(topic = "c.ExerciseSell")
public class ExerciseSell {
    // 线程安全
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow window = new TicketWindow(10);
        // 卖出的票数统计
        List<Integer> amountList = new Vector<>();
        // 线程等待集合
        List<Thread> threadList = new ArrayList<>(1000);
        for(int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int amount = window.sell(randomAmount());
                try {
                    Thread.sleep(randomAmount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                amountList.add(amount);
            });
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            thread.join();
        }
        log.debug("余票： {}", window.getTicketCount());
        log.debug("卖出： {}", amountList.stream().mapToInt(i-> i).sum());
    }

    public static int randomAmount() {
        return random.nextInt(5) + 1;
    }
}

class TicketWindow {

    private int ticketCount;

    public TicketWindow(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getTicketCount() {
        return this.ticketCount;
    }

    public synchronized int sell(int amount) {
        if (this.ticketCount >= amount) {
            this.ticketCount -= amount;
            return amount;
        } else {
            return 0;
        }
    }

}
