package pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestBlockQueue")
public class TestBlockQueue {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 1) 死等
//            queue.put(task);
            // 2） 带超时等待
//            queue.offer(task, 1000, TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃调用
//            log.debug("放弃调用");
            // 4） 让调用者抛异常
//            throw new RuntimeException("任务执行失败:" + task);
            // 5) 让调用者自己执行任务
//            task.run();
        });
        for(int i = 0; i < 3; i++) {
            int j = i+1;
            pool.execute(() -> {
                try {
                    Thread.sleep(1000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("任务-》{}", j);
            });
        }
    }
}

// 策略模式，给用户处理
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j(topic = "c.ThreadPool")
class ThreadPool {
    // 核心线程数
    private int coreSize;
    // 线程集合
    private HashSet<Worker> workers = new HashSet();
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;
    // 获取任务时的超时时间
    private long timeout;
    // 时间单位
    private TimeUnit timeUnit;
    private RejectPolicy<Runnable> rejectPolicy;

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数没超过coreSize时，直接交给Worker执行
        // 任务数超过coreSize时，进入任务队列
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增任务： {}", task);
                workers.add(worker);
                worker.start();
            } else {
//                taskQueue.put(task);
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity,
                      RejectPolicy rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 当task不为空，执行任务
            // 当task为空，从任务队列中获取任务并执行
//            while(task != null || ((task = taskQueue.take()) != null)) {
            while(task != null || ((task = taskQueue.pool(timeout, timeUnit)) != null)) {
                try {
                    log.debug("正在执行： {}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker被移除：{}",this);
                workers.remove(this);
            }
        }
    }
}

@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T> {
    // 任务队列
    private Deque<T> queue = new ArrayDeque<>();
    // 锁
    private ReentrantLock lock = new ReentrantLock();
    // 消费者等待
    private Condition emptyWaitSet = lock.newCondition();
    // 生产者等待
    private Condition fullWaitSet = lock.newCondition();
    // 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T pool(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while(queue.isEmpty()) {
                try {
                    if (nanos < 0) {
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            fullWaitSet.signal();
            return  first;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞获取
    public T take() {
        lock.lock();
        try {
            while(queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T first = queue.removeFirst();
            fullWaitSet.signal();
            return  first;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T task) {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                try {
                    log.debug("等待加入队列...");
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("进入队列： {}", task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 带超时时间的阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while(queue.size() == capacity) {
                try {
                    if (nanos < 0) {
                        return false;
                    }
                    log.debug("等待加入队列...");
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("进入队列： {}", task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {
                queue.addLast(task);
                log.debug("进入队列： {}", task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
