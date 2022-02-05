package nio.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHandler implements Runnable {

    private ChatClient chatClient;

    public UserInputHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // 读取输入消息
                String inputMsg = consoleReader.readLine();
                chatClient.send(inputMsg);

                // 判断是否退出
                if (chatClient.readyToQuit(inputMsg)) {
                    break;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
