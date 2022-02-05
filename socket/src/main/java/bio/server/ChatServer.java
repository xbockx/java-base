package bio.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private int DEFAULT_PORT = 8888;
    private final String QUIT = "quit";

    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private Map<Integer, Writer> connectedClients;

    public ChatServer() {
        connectedClients = new HashMap<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    /**
     * 添加客户
     *
     * @param socket
     * @throws IOException
     */
    public synchronized void addClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            connectedClients.put(port, bufferedWriter);
            System.out.println("客户端[" + port + "]: 已连接");
        }
    }

    /**
     * 移除客户
     *
     * @param socket
     * @throws IOException
     */
    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null) {
            int port = socket.getPort();
            if (connectedClients.containsKey(port)) {
                connectedClients.get(port).close();
            }
            connectedClients.remove(port);
            System.out.println("客户端[" + port + "]: 已断开");
        }
    }

    /**
     * 转发消息
     *
     * @param socket
     * @param msg
     * @throws IOException
     */
    public synchronized void forwardMessage(Socket socket, String msg) throws IOException {
        for (Integer id : connectedClients.keySet()) {
            if (!id.equals(socket.getPort())) {
                Writer writer = connectedClients.get(id);
                writer.write(msg);
                writer.flush();
            }
        }
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
     * 关闭服务
     */
    public synchronized void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                System.out.println("*服务已关闭 监听端口: " + DEFAULT_PORT);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * 启动服务
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("*服务已启动 监听端口: " + DEFAULT_PORT);
            while (true) {
                Socket accept = serverSocket.accept();
                // 创建ChatHandler线程
                executorService.execute(new ChatHandler(this, accept));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
