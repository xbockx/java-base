package tube;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestSyncAwaitSignal {
    public static void main(String[] args) throws InterruptedException {
        SyncAwaitSignal sas = new SyncAwaitSignal(3);
        Condition a = sas.newCondition();
        Condition b = sas.newCondition();
        Condition c = sas.newCondition();
        new Thread(() -> {
            sas.print("a", a, b);
        }, "t1").start();
        new Thread(() -> {
            sas.print("b", b, c);
        }, "t2").start();
        new Thread(() -> {
            sas.print("c", c, a);
        }, "t3").start();

        Thread.sleep(1000);
        sas.lock();
        try {
            a.signal();
        } finally {
            sas.unlock();
        }
    }
}

class SyncAwaitSignal extends ReentrantLock {
    private int loopNumber;

    public SyncAwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String msg, Condition cur, Condition next) {
        for(int i = 0; i < loopNumber; i++) {
            lock();
            try {
                cur.await();
                System.out.print(msg);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}
