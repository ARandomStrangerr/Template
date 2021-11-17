package connection_and_storage.connection.listener;

import connection_and_storage.connection.socket.SocketInterface;
import connection_and_storage.connection.socket.PlainSocket;
import connection_and_storage.storage.GroupStorage;
import connection_and_storage.storage.SingleStorage;
import connection_and_storage.storage.StorageInterface;
import connection_and_storage.storage.StorageType;

import javax.management.InstanceAlreadyExistsException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public abstract class Listener<T extends SocketInterface> implements ListenerInterface<T> {

    private final ServerSocket socket;
    private final StorageInterface<T> connectionStorage;

    public Listener(int port,
                    StorageType type)
            throws IOException {
        this.socket = new ServerSocket(port);
        switch (type) {
            case GROUP:
                connectionStorage = new GroupStorage<>();
                break;
            case SINGLE:
                connectionStorage = new SingleStorage<>();
                break;
            default:
                throw new IllegalArgumentException("The given enum is incorrect, WHICH IS LITERALLY IMPOSSIBLE");
        }
    }

    /**
     * put a given connection to this collection of connection
     *
     * @param key    key associated with the connection
     * @param socket socket that associated with the connection
     */
    @Override
    public void put(String key, T socket) {
        connectionStorage.put(key, socket);
    }

    protected Socket acceptSocket() throws IOException{
        return socket.accept();
    }

    /**
     * get a socket with a given name, behaviour is different based on the storage structure
     *
     * @param key get a socket under the key name
     * @return a socket under the key name
     * @throws NullPointerException there is no socket associated with such name
     */
    @Override
    public T get(String key) throws NullPointerException {
        return connectionStorage.get(key);
    }

    /**
     * remove a socket from the collection
     *
     * @param socket the socket to be removed
     */
    @Override
    public void remove(T socket) {
        connectionStorage.remove(socket);
    }

    /**
     * close this listener and all the sockets that associated with it
     *
     * @throws IOException when error to close one of the socket or close the listener
     */
    @Override
    public void close() throws IOException {
        for(T socket : connectionStorage) {
            socket.close();
        }
        socket.close();
    }
}
