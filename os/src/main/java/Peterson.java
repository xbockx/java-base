/**
 * @Description
 * @Author xbockx
 * @Date 3/24/2022
 */
public class Peterson {
    private static final int T1 = 0;
    private static final int T2 = 1;

    // 保证可见性
    private static volatile boolean[] flag = new boolean[2];
    private static volatile int turn;

    private static long delay = 0;
    private static int balance = 10000;
    private static int tradeAmount = 1;
    private static int N = 50000;

    public static void main(String[] args) {
        peterson();
        unsafe();
    }

    public static void unsafe() {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < N; i++) {
                balance += tradeAmount;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < N; i++) {
                balance -= tradeAmount;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t2.join();
            t1.join();
            System.out.println("unsafe: " + balance);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void peterson() {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < N; i++) {
                flag[T1] = true;
                turn = T2;
                while(flag[T2] && turn == T2) {
                    System.out.println("t1 wait");
                }
                balance -= tradeAmount;
                flag[T1] = false;

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < N; i++) {
                flag[T2] = true;
                turn = T1;
                while(flag[T1] && turn == T1) {
                    System.out.println("t2 wait");
                }
                balance += tradeAmount;
                flag[T2] = false;

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        try {
            t2.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("peterson: " + balance);
    }
}
