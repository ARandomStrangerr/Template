package connection_and_storage.storage;

import connection_and_storage.connection.socket.SocketInterface;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class SingleStorage<T extends SocketInterface> implements StorageInterface<T> {
    private final Hashtable<String, T> storageStructure;

    public SingleStorage() {
        storageStructure = new Hashtable<>();
    }

    @Override
    public void put(String key, T socket) {
        storageStructure.put(key, socket);
    }

    @Override
    public T get(String key)
            throws NoSuchElementException {
        if (storageStructure.containsKey(key))
            return storageStructure.get(key);
        throw new NoSuchElementException("No such key matched with the given key");
    }

    /**
     * this method is not available for this class
     *
     * @param key      name of the group
     * @param hashCode unique code of the socket
     * @return nothing
     * @throws IllegalAccessException always throw this exception
     */
    @Override
    public T get(String key,
                 int hashCode)
            throws IllegalAccessException {
        throw new IllegalAccessException("this method is not available for single storage structure");
    }

    @Override
    public void remove(String key) {
        storageStructure.remove(key);
    }

    /**
     * always throw IllegalAccessException
     *
     * @param key      name of the group
     * @param hashCode unique code of the socket
     * @throws IllegalAccessException always throw this exception
     */
    @Override
    public void remove(String key,
                       int hashCode)
            throws IllegalAccessException {
        throw new IllegalAccessException("This method is not available for single storage structure");
    }

    /**
     * the universal way to remove socket within collection
     * @param socket the socket needed to be removed
     * @throws NoSuchElementException when the socket cannot be found in the collection
     */
    @Override
    public void remove(T socket) throws NoSuchElementException {
        remove(socket.getKey());
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
