package socket;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * store socket under a unique name
 */
public class OneToOneStorage implements StorageInterface {
    private final ConcurrentHashMap<String, Socket> storageStructure;
    OneToOneStorage(){
        storageStructure = new ConcurrentHashMap<>();
    }

    /**
     * one-key-to-one-socket: get a socket under the associated name.
     * <br>
     * one-key-to-many-socket: get a socket within the group under associated name.
     *
     * @param key name of the socket
     * @return socket for one-to-one: return a socket with the given name.
     * <br>
     * for one-to-many: return a socket within the group with the given name, such socket is processing the least
     * instances of requests.
     * @throws NullPointerException when there is no key is associated with the given name.
     *                              this error only throw for one-to-one structure.
     */
    @Override
    public Socket get(String key) throws NullPointerException {

        return null;
    }

    /**
     * only available for one-key-to-many-socket
     * get a socket as a combination of its group name and its unique id with in the group.
     * <br>
     * usage: use when return request specify an instance of socket
     *
     * @param key name of the group;
     * @param id  unique id of the socket
     * @return socket associated with the given key
     * @throws IllegalArgumentException when try to access this method from one-key-to-one-socket class
     */
    @Override
    public Socket get(String key, int id) throws IllegalAccessException {
        throw new IllegalAccessException("This method is not designed for OneToOneStorage");
    }

    /**
     * one-key-to-one-socket: store the given socket under the given key
     * <br>
     * one-key-to-many-socket: store the given socket within the given name group
     *
     * @param key    one-to-one: name of the socket.
     *               one-to-many: name of the group of socket.
     * @param socket the socket to store
     * @throws IllegalArgumentException only for one-to-one: when the given key already associated with a socket
     */
    @Override
    public void add(String key, Socket socket) throws IllegalArgumentException {
        if (storageStructure.containsKey(key)) throw new IllegalArgumentException("Given key already is associated with a socket");
        storageStructure.put(key, socket);
    }

    /**
     * only available for one-to-one storage
     * remove the socket with the associated name
     *
     * @param key name of the socket
     * @return removed socket
     * @throws NullPointerException when there is no such socket to remove
     */
    @Override
    public Socket remove(String key) throws NullPointerException {
        Socket removedItem = storageStructure.remove(key);
        if (removedItem == null) throw new NullPointerException("The given key is not associated with any socket within the storage");
        return removedItem;
    }

    /**
     * only available for one-to-many storage
     * remove the socket within the group name with associated id
     *
     * @param key group that contains the socket
     * @param id  unique id of the socket
     * @throws NullPointerException when there is no such socket to remove.
     * @throws IllegalAccessException Always throw it in this class
     */
    @Override
    public Socket remove(String key, int id) throws IllegalAccessException {
        throw new IllegalAccessException("This method does not design for OneToOne storage");
    }

    /**
     * general remove case. this method works for both one-to-one and one-to-many storage.
     * this wrapper method call appropriate method based on the type of storage.
     *
     * @param socket socket to remove
     * @return the removed socket from collection
     */
    @Override
    public Socket remove(Socket socket) {
        return this.remove(socket.getName());
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Socket> iterator() {
        LinkedList<Socket> socketList = new LinkedList<>();
        for(Map.Entry<String, Socket> entry : storageStructure.entrySet()) socketList.add(entry.getValue());
        return socketList.iterator();
    }
}
