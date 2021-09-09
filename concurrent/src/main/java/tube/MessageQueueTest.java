package tube;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j(topic = "c.MessageQueueTest")
public class MessageQueueTest {
    public static void main(String[] args) {
        MessageQueue mq = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                mq.put(new Message(id, "消息" + id));
            }, "生产者" + i).start();
        }
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    Message message = mq.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "消费者").start();
    }
}

// 线程间的消息队列
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    private LinkedList<Message> queue = new LinkedList<>();
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public Message take() {
        synchronized (queue) {
            while (queue.size() == 0) {
                log.debug("队列空！");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.removeFirst();
            log.debug("消费： {}", message);
            queue.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() >= capacity) {
                log.debug("队列满！");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(message);
            log.debug("生产: {}", message);
            queue.notifyAll();
        }
    }
}

// 消息类
final class Message {
    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message=" + message +
                '}';
    }
}
