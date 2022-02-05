package bio.pre;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String DEFAULT_SERVER_HOST = "127.0.0.1";
        final int DEFAULT_SERVER_PORT = 8888;
        final String QUIT = "quit";
        Socket socket = null;
        BufferedWriter writer = null;

        try {
            socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // get user input
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                String msg = userInput.readLine();

                // send message
                writer.write(msg + "\n");
                writer.flush();

                // receive message
                String res = reader.readLine();
                System.out.println(res);

                if(QUIT.equals(msg)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                System.out.println("关闭socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
