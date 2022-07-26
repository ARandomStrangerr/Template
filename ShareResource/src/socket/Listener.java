package socket;

import java.io.IOException;
import java.net.ServerSocket;

public class Listener {
    private final ServerSocket listener;
    private final StorageInterface storage;

    /**
     * crate a plain socket - no encryption
     *
     * @param port port in which the socket will be operated
     * @throws IOException              when the given port is in use already
     * @throws IllegalArgumentException when the given type of the storage is incorrect
     */
    public Listener(
            int port,
            StorageType storageType
    ) throws IOException, IllegalArgumentException {
        this.listener = new ServerSocket(port);
        switch (storageType) {
            case ONE_TO_ONE:
                storage = new OneToOneStorage();
                break;
            case ONE_TO_MANY:
                storage = new OneToManyStorage();
                break;
            default:
                throw new IllegalArgumentException("The storage type param is incorrect");
        }
    }

    public Listener(
        KeyStore
    )

    //todo include ssl socket

    /**
     * accept an incoming socket to the listener
     *
     * @return the accepted socket
     * @throws IOException when fail to accept the socket
     */
    public Socket accept() throws IOException {
        return new Socket(listener.accept());
    }

    /**
     * close this listener and all the sockets which accepted by this listener.
     *
     * @throws IOException when this socket is failed to close, some memory will be leaked since some socket will not be closed
     */
    public void close() throws IOException {
        listener.close();
        for (Socket socket : storage){
            socket.close();
        }
    }

    public void putSocket(String key, Socket socket) throws IllegalArgumentException{
        this.storage.add(key, socket);
    }

    public Socket getSocket(String key) throws NullPointerException {
        return storage.get(key);
    }

    public Socket getSocket(String key, int id) {
        try {
            return storage.get(key, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Socket removeSocket(Socket socket){
        return storage.remove(socket);
    }
}
