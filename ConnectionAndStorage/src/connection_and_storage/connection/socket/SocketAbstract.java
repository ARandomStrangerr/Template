package connection_and_storage.connection.socket;

import connection_and_storage.connection.listener.Listener;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public abstract class SocketAbstract implements SocketInterface {
    private final Socket socket;
    private final BufferedReader br;
    private final BufferedWriter bw;
    private volatile int counter;
    private String key;

    /**
     * @param socket the socket which is instantiated
     * @throws IOException when cannot initiate Buffer to read and write
     */
    public SocketAbstract(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        counter = 0;
    }

    /**
     * set the key (name) of this socket. This name will be used to set the name of this socket when added to collection
     * {@link Listener} this must be set otherwise the socket will throw error
     *
     * @param key name of this socket
     */
    @Override
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the name declared for this socket
     * @throws NullPointerException when the name has not been set
     */
    @Override
    public String getKey() throws NullPointerException {
        if (this.key == null) throw new NullPointerException("This socket's key (name) has not been initialized");
        return this.key;
    }

    /**
     * set the timeout of the socket when it does not receive anything
     *
     * @param millisecond the amount of time to wait before throws an {@link SocketTimeoutException}
     */
    @Override
    public void setSoTimeout(int millisecond) throws IOException {
        socket.setSoTimeout(millisecond);
    }

    /**
     * read data from the socket
     *
     * @return data from the stream
     * @throws IOException when the data cannot be written
     */
    @Override
    public String read() throws IOException {
        String read = br.readLine();
        if(read == null) throw new IOException("Broken pipe. Probably the other-side socket is closed");
        return read;
    }

    /**
     * write data to the socket
     *
     * @param data data to write to the socket
     * @throws IOException when the data cannot be written
     */
    @Override
    public synchronized void write(String data) throws IOException {
        bw.write(data);
        bw.newLine();
        bw.flush();
    }

    /**
     * increase the priority of the socket
     */
    @Override
    public synchronized void increaseCounter() {
        counter++;
    }

    /**
     * decrease the priority of the socket
     */
    @Override
    public synchronized void decreaseCounter() {
        counter--;
    }

    /**
     * @return number indicating priority of a socket
     */
    @Override
    public synchronized int getCounter() {
        return counter;
    }

    /**
     * close this socket then remove this instance from the storage
     *
     * @throws IOException when fail to close the socket
     */
    @Override
    public void close() throws IOException {
        br.close();
        bw.close();
        socket.close();
    }
}
