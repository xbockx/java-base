package jmm;

public class TestSingleton {
    public static void main(String[] args) {
        Singleton instance = Singleton.getInstance();
    }
}

final class Singleton {
    private Singleton() {};
    private static volatile Singleton INSTANCE = null;

    public static Singleton getInstance() {
        // 实例没创建，才会进入synchronized代码块
        if(INSTANCE == null) {
            synchronized(Singleton.class) {
                // 也许有其他线程创建了实例，所以再判断一次
                if(INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
