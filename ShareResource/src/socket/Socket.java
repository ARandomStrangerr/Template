package socket;

import java.io.*;
import java.net.InetAddress;

/**
 * a wrapper class for the {@link java.net.Socket}.
 * make the socket is easier to use rater than have to worry about reader and writer streams.
 */
public class Socket {
    private final java.net.Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private volatile int activeRequest;
    private String name;

    /**
     * create a wrapper of the given socket
     *
     * @param socket created socket
     * @throws IOException when fail to open reader or writer
     */
    public Socket(java.net.Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        activeRequest = 0;
    }

    /**
     * create a connection from client -> host.
     * create a plain socket from given address and port.
     *
     * @param address address of the host
     * @param port    port which except this connection
     * @throws IOException when the address and port does not accept this connection. reader, or writer cannot be created.
     */
    public Socket(InetAddress address, int port) throws IOException {
        this(new java.net.Socket(address, port));
    }

    /**
     * one line of string (one package) received from this socket
     *
     * @return string is read from this socket
     * @throws IOException error raise when the socket is closed
     */
    public String read() throws IOException {
        return reader.readLine();
    }

    /**
     * send data from this socket
     *
     * @param data to write to this socket
     * @throws IOException error raise when the socket is closed
     */
    public synchronized void write(String data) throws IOException {
        writer.write(data);
        writer.newLine();
        writer.flush();
    }

    /**
     * set the mount of time for socket to declare its timeout
     * @param millisecond amount of time
     * @throws IOException when the socket is closed
     */
    public void setTimeout(int millisecond) throws IOException{
        socket.setSoTimeout(millisecond);
    }

    /**
     * @return an integer response to number of active requests, which service responding to this socket is processing
     */
    public int getActiveRequest() {
        return activeRequest;
    }

    /**
     * increase the number of active requests, which service responding to this socket is processing, by 1
     */
    public synchronized void increaseActiveRequest() {
        activeRequest++;
    }

    /**
     * decrease the number of active request, which service responding to this socket, by 1
     */
    public synchronized void decreaseActiveRequest() {
        activeRequest--;
    }

    /**
     * close this socket
     *
     * @throws IOException error raise when the socket is closed
     */
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

    /**
     * @return get the name of this socket or the group this socket belongs to
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of this socket or the name of the group this socket belongs to
     *
     * @param name name of this socket / group of sockets
     */
    public void setName(String name) {
        this.name = name;
    }
}
