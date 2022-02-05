package nio.pre;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

interface FileCopyRunner {
    void copy (File source, File target);
}

public class FileCopyDemo {

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FileCopyRunner noBufferStream = new FileCopyRunner() {
            @Override
            public void copy(File source, File target) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = new FileInputStream(source);
                    out = new FileOutputStream(target);

                    int result;
                    while((result = in.read()) != -1) {
                        out.write(result);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }
        };

        FileCopyRunner BufferedStream = new FileCopyRunner() {
            @Override
            public void copy(File source, File target) {
                InputStream in = null;
                OutputStream out = null;

                try {
                    in = new BufferedInputStream(new FileInputStream(source));
                    out = new BufferedOutputStream(new FileOutputStream(target));

                    byte[] buffer = new byte[1024];
                    int result;
                    while((result = in.read(buffer)) != -1) {
                        out.write(buffer, 0, result);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }
        };

        FileCopyRunner nioBufferChannel = new FileCopyRunner() {
            @Override
            public void copy(File source, File target) {
                FileChannel in = null;
                FileChannel out = null;

                try {
                    in = new FileInputStream(source).getChannel();
                    out = new FileOutputStream(target).getChannel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    while(in.read(buffer) != -1) {
                        buffer.flip();
                        while(buffer.hasRemaining()) {
                            out.write(buffer);
                        }
                        buffer.clear();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }
        };

        FileCopyRunner nioTransferChannel = new FileCopyRunner() {
            @Override
            public void copy(File source, File target) {
                FileChannel in = null;
                FileChannel out = null;

                try {
                    in = new FileInputStream(source).getChannel();
                    out = new FileOutputStream(target).getChannel();

                    long transfer = 0L;
                    long size = in.size();
                    while(transfer != size) {
                        transfer += in.transferTo(0, size, out);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    close(in);
                    close(out);
                }
            }
        };

    }
}
