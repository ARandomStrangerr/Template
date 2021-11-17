package connection_and_storage.connection.listener;

import connection_and_storage.connection.socket.SocketInterface;

import javax.management.InstanceAlreadyExistsException;
import java.io.IOException;
import java.util.NoSuchElementException;

public interface ListenerInterface<T extends SocketInterface> {
    /**
     * accept an incoming connection
     *
     * @return an established socket with by this listener
     * @throws IOException when fail to establish the socket
     */
    T accept()
            throws IOException;

    /**
     * put a given connection to this collection of connection
     *
     * @param key    key associated with the connection
     * @param socket socket that associated with the connection
     */
    void put(String key,
             T socket);

    /**
     * get a socket with a given name, behaviour is different based on the storage structure
     *
     * @param key get a socket under the key name
     * @return a socket under the key name
     * @throws NoSuchElementException there is no socket associated with such name
     */
    T get(String key)
            throws NoSuchElementException;

    /**
     * get a socket with a given name and code, this only apply for group storage
     *
     * @param key      the group that contain the socket
     * @param hashCode the code identify a socket within group
     * @return a socket of combination of its key and hash code
     * @throws NullPointerException when the group or the key does not responsible for any element
     * @throws IllegalAccessException this method only available for group storage
     */
    T get(String key, int hashCode)
            throws NoSuchElementException,
            IllegalAccessException;

    /**
     * remove a socket from the collection
     *
     * @param socket the socket to be removed
     */
    void remove(T socket);

    /**
     * close this listener and all the sockets that associated with it
     *
     * @throws IOException when error to close one of the socket or close the listener
     */
    void close()
            throws IOException;
}
