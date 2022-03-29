/**
 * @Description Producer and Consumer question
 * @Author xbockx
 * @Date 3/25/2022
 */
public class PC {
    private int count = 0;
    private int n = 8;
    Object lock = new Object();

    public static void main(String[] args) {
        PC pc = new PC();
        Thread t1 = new Thread(() -> {
            pc.producer();
        });
        Thread t2 = new Thread(() -> {
            pc.consumer();
        });
        Thread t3 = new Thread(() -> {
            pc.consumer();
        });
        Thread t4 = new Thread(() -> {
            pc.consumer();
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    public void producer() {
        while(true) {
            synchronized (lock) {
                while(count == n) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notifyAll();
                count++;
                System.out.print("(");
            }
        }
    }

    public void consumer() {
        while (true) {
            synchronized (lock) {
                while(count == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notifyAll();
                count--;
                System.out.print(")");
            }
        }
    }
}
