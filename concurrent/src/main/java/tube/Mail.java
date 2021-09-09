package tube;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.Mail")
public class Mail {
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 3; i++) {
            new People().start();
        }
        Thread.sleep(1000);
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容"+id).start();
        }
    }
}

@Slf4j(topic = "c.People")
class People extends Thread {
    @Override
    public void run() {
        GuardedObject guardedObject = MailBoxes.createGuardedObject();
        log.debug("开始收信... ID: {}", guardedObject.getId());
        Object response = guardedObject.getResponse(5000);
        log.debug("完成收信... ID: {}, 内容: {}", guardedObject.getId(), response);
    }
}

@Slf4j(topic = "c.Postman")
class Postman extends Thread {
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = MailBoxes.getGuardedObject(id);
        log.debug("送信... ID: {}, 内容: {}", id, mail);
        guardedObject.setResponse(mail);
    }
}

class MailBoxes {
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    // 生成自增ID
    public static synchronized int generateId() {
        return id++;
    }

    // 创建GuardedObject对象
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }


    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}

class GuardedObject {

    //唯一标识
    private int id;
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public synchronized void setResponse(Object response) {
        this.response = response;
        this.notifyAll();
    }

    public synchronized Object getResponse(long timeout) {
        long begin = System.currentTimeMillis();
        long passedTime = 0;
        while(response == null) {
            long delay = timeout - passedTime;
            if (delay <= 0) {
                break;
            }
            try {
                this.wait(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passedTime = System.currentTimeMillis() - begin;
        }
        return this.response;
    }
}
