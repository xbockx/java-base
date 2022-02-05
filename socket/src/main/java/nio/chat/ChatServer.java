package nio.chat;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @Description
 * @Author xbockx
 * @Date 1/30/2022
 */
public class ChatServer {

    private static final int DEFAULT_PORT = 8888;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private int port;
    private ServerSocketChannel server;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public ChatServer() {
        this(DEFAULT_PORT);
    }

    public ChatServer(int port) {
        this.port = port;
    }

    private void start() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port));

            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("*服务已启动 监听端口: " + port);

            while(true) {
                selector.select();
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    handle(selectionKey);
                }
                selectionKeys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(selector);
        }
    }

    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端[" + client.socket().getPort() + "]: 已连接");
        }
        if (selectionKey.isReadable()) {
            SocketChannel client = (SocketChannel) selectionKey.channel();
            String fwdMsg = receive(client);
            if (fwdMsg.isEmpty()) {
                selectionKey.cancel();
                selector.wakeup();
            } else {
                forward(client, fwdMsg);

                if (readyToQuit(fwdMsg)) {
                    selectionKey.cancel();
                    selector.wakeup();
                    System.out.println("客户端[" + port + "]: 已断开");
                }
            }
        }
    }

    private void forward(SocketChannel client, String fwdMsg) throws IOException {
        final Set<SelectionKey> selectionKeys = selector.keys();
        for (SelectionKey key : selectionKeys) {
            Channel channel = key.channel();
            if (channel instanceof ServerSocketChannel) {
                continue;
            }
            if (key.isValid() && !client.equals(channel)) {
                wBuffer.clear();
                wBuffer.put(charset.encode("客户端[" + client.socket().getPort() + "]: " + fwdMsg));
                wBuffer.flip();

                while (wBuffer.hasRemaining()) {
                    ((SocketChannel)channel).write(wBuffer);
                }
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while (client.read(rBuffer) > 0);
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
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
    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                System.out.println("*关闭连接");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
