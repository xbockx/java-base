package tube;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Slf4j(topic = "c.Pattern")
public class Pattern {
    public static void main(String[] args) {
        GuardedObject2<List<String>> object = new GuardedObject2<>();
        new Thread(() -> {
            log.debug("等待结果");
            List<String> response = object.getResponse();
            log.debug("结果大小： {}", response.size());
        }, "t1").start();
        new Thread(() -> {
            log.debug("开始下载");
            Downloader downloader = new Downloader();
            try {
                List<String> download = downloader.download();
                object.setResponse(download);
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.debug("下载完毕");
        }, "t2").start();
    }
}

class GuardedObject2<T> {
    private T response;

    public synchronized T getResponse() {
        while (response == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.response;
    }

    public synchronized void setResponse(T response) {
        this.response = response;
        this.notifyAll();
    }
}

// 模拟下载
class Downloader {
    public List<String> download() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
        List<String> list = new LinkedList<>();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
}
