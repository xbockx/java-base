package tube;

public class TestSyncWaitNotify {
    public static void main(String[] args) {
        SyncWaitNotify swn = new SyncWaitNotify(1, 3);
        new Thread(() -> {
            swn.print("a", 1, 2);
        }, "t1").start();
        new Thread(() -> {
            swn.print("b", 2, 3);
        }, "t2").start();
        new Thread(() -> {
            swn.print("c", 3, 1);
        }, "t3").start();
    }
}

class SyncWaitNotify {
    // 等待标记
    private int flag;
    private int loopNumber;

    public SyncWaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(String msg, int curFlag, int nextFlag) {
        for(int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (flag != curFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(msg);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}