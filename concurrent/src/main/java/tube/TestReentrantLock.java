package tube;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {
    public static void main(String[] args) {
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");

        new Philosopher2("苏格拉底", c1, c2).start();
        new Philosopher2("赫拉克利特", c2, c3).start();
        new Philosopher2("柏拉图", c3, c4).start();
        new Philosopher2("亚里士多德", c4, c5).start();
        new Philosopher2("阿基米德", c5, c1).start();
    }
}

@Slf4j(topic = "c.Philosopher2")
class Philosopher2 extends Thread {
    private Chopstick2 left;
    private Chopstick2 right;

    public Philosopher2(String name, Chopstick2 left, Chopstick2 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while(true) {
            if(left.tryLock()) {
                try {
                    if (right.tryLock()) {
                        try {
                            eat();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            // 释放锁s
                            right.unlock();
                        }
                    }
                } finally {
                    // 释放锁
                    left.unlock();
                }
            }
        }
    }

    public void eat() {
        log.debug("eating...");
    }
}

class Chopstick2 extends ReentrantLock {
    private String name;

    public Chopstick2(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子" +
                "name='" + name + '\'';
    }
}
