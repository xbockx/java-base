package bio.client;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private final int DEFAULT_SERVER_PORT = 8888;
    private final String QUIT = "quit";

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * 发送消息
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException {
        if (!socket.isOutputShutdown()) {
            writer.write(msg + "\n");
            writer.flush();
        }
    }

    /**
     * 接收消息
     * @return
     * @throws IOException
     */
    public String receive() throws IOException {
        String msg = null;
        if (!socket.isInputShutdown()) {
            msg = reader.readLine();
        }
        return msg;
    }

    /**
     * 判断是否退出
     * @param msg
     * @return
     */
    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg.trim());
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (socket != null) {
            try {
                socket.close();
                System.out.println("*关闭连接");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void start() {
        try {
            // 创建连接
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);

            // 创建I/O流
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // 处理用户输入
            new Thread(new UserInputHandler(this)).start();

            // 读取服务器转发的消息
            String msg = null;
            while((msg = receive()) != null) {
                System.out.println(msg);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
