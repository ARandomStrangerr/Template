package connection_and_storage.storage;

import connection_and_storage.connection.socket.SocketInterface;

import java.util.Hashtable;
import java.util.Iterator;
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
        if (storageStructure.contains(key))
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
        throw new IllegalAccessException();
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
        throw new IllegalAccessException();
    }

    @Override
    public void remove(T socket) throws NoSuchElementException {

    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}
