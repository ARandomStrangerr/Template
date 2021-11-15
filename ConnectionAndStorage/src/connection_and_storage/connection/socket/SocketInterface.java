package connection_and_storage.connection.socket;

import java.io.IOException;

public interface SocketInterface {
    /**
     * set the key (name) of this socket. This name will be used to set the name of this socket when added to collection
     * {@link connection_and_storage.connection.listener.Listener} this must be set otherwise the socket will throw error
     *
     * @param key name of this socket
     */
    void setKey(String key);

    /**
     * @return the name declared for this socket
     * @throws NullPointerException when the name has not been set
     */
    String getKey() throws NullPointerException;

    /**
     * set the timeout of the socket when it does not receive anything
     * @param millisecond the amount of time to wait before throws an {@link java.net.SocketTimeoutException}
     */
    void setSoTimeout(int millisecond) throws IOException;

    /**
     * write data to the socket
     *
     * @param data data to write to the socket
     * @throws IOException when the data cannot be written
     */
    void write(String data) throws IOException;

    /**
     * read data from the socket
     *
     * @return data from the stream
     * @throws IOException when the data cannot be written
     */
    String read() throws IOException;

    /**
     *
     * @return number indicating priority of a socket
     */
    int getPriority();

    /**
     * increase the priority of the socket
     */
    void increasePriority();

    /**
     * decrease the priority of the socket
     */
    void decreasePriority();

    /**
     * close this socket then remove this instance from the storage
     *
     * @throws IOException when fail to close the socket
     */
    void close() throws IOException;
}