package bio.pre;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        ServerSocket server = null;
        final String QUIT = "quit";

        try {
            server = new ServerSocket(DEFAULT_PORT);
            System.out.println("启动服务器， 监听端口：" + DEFAULT_PORT);
            while (true) {
                Socket socket = server.accept();
                System.out.println("客户端[" + socket.getPort() + "]已连接");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String msg = null;
                while((msg = reader.readLine()) != null) {
                    if(QUIT.equals(msg)) {
                        writer.write("服务器：bye!\n");
                        writer.flush();
                        System.out.println("客户端[" + socket.getPort() + "]断开连接");
                        break;
                    }

                    System.out.println("客户端[" + socket.getPort() + "]: " + msg);
                    writer.write("服务器："+ msg + "\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null) {
                try {
                    server.close();
                    System.out.println("服务器关闭");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
