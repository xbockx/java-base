package bio.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatHandler implements Runnable {

    private ChatServer chatServer;
    private Socket socket;

    public ChatHandler(ChatServer chatServer, Socket socket) {
        this.chatServer = chatServer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            chatServer.addClient(socket);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg = null;
            while((msg = reader.readLine()) != null) {
                // 格式化消息
                String fwdMsg = "客户端[" + socket.getPort() + "]: " + msg + "\n";
                System.out.println(fwdMsg);
                // 发送消息
                chatServer.forwardMessage(socket, fwdMsg);
                // 判断是否退出
                if (chatServer.readyToQuit(msg)) {
                    break;
                }
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                chatServer.removeClient(socket);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
