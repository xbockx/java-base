package nio.chat;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @Description
 * @Author xbockx
 * @Date 1/30/2022
 */
public class ChatClient {

    private static final int DEFAULT_PORT = 8888;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String QUIT = "quit";
    private static final int BUFFER = 1024;

    private int port;
    private String host;
    private SocketChannel client;
    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER);
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public ChatClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void start() {
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);

            selector = Selector.open();
            client.register(selector, SelectionKey.OP_CONNECT);
            client.connect(new InetSocketAddress(host, port));

            while (true) {
                selector.select();
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    handle(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClosedSelectorException e) {
        } finally {
            close(selector);
        }
    }

    public void send(String inputMsg) throws IOException {
        if (inputMsg.isEmpty()) {
            return;
        }

        wBuffer.clear();
        wBuffer.put(charset.encode(inputMsg));
        wBuffer.flip();
        while(wBuffer.hasRemaining()) {
            client.write(wBuffer);
        }

        if (readyToQuit(inputMsg)) {
            close(selector);
            System.out.println("*关闭连接");
        }
    }

    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isConnectable()) {
            final SocketChannel channel = (SocketChannel) selectionKey.channel();
            if (channel.isConnectionPending()) {
                channel.finishConnect();
                new Thread(new UserInputHandler(this)).start();
            }
            client.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            SocketChannel client = (SocketChannel) selectionKey.channel();
            String fwdMsg = receive(client);
            if (fwdMsg.isEmpty()) {
                close(selector);
            } else {
                System.out.println(fwdMsg);
            }
        }
    }

    private String receive(SocketChannel client) throws IOException {
        rBuffer.clear();
        while(client.read(rBuffer) > 0);
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
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ChatClient chatClient = new ChatClient();
        chatClient.start();
    }

}
