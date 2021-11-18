package connection_and_storage.storage;

import connection_and_storage.connection.socket.SocketInterface;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class GroupStorage<T extends SocketInterface> implements StorageInterface<T> {
    private final Hashtable<String, Hashtable<Integer, T>> storageStructure;

    public GroupStorage() {
        this.storageStructure = new Hashtable<>();
    }

    @Override
    public void put(String key,
                    T socket) {
        if (this.storageStructure.contains(key)) {
            storageStructure.get(key).put(socket.hashCode(), socket);
        } else {
            Hashtable<Integer, T> temp = new Hashtable<>();
            temp.put(socket.hashCode(), socket);
            storageStructure.put(key, temp);
        }
    }

    /**
     * @param key name of the socket
     * @return return a socket within the lowest priority within the pool
     * @throws NoSuchElementException when the given key does not match with any group
     */
    @Override
    public T get(String key)
            throws NoSuchElementException {
        if (!storageStructure.contains(key)) throw new NoSuchElementException();
        T returnSocket = null;
        for (Map.Entry<Integer, T> socket : storageStructure.get(key).entrySet()) {
            if (returnSocket == null || returnSocket.getCounter() < socket.getValue().getCounter())
                returnSocket = socket.getValue();
        }
        return returnSocket;
    }

    /**
     * @param key      name of the group
     * @param hashCode unique code of the socket
     * @return a socket associated with group name and its unique identification
     * @throws NoSuchElementException when the key or code does not response to a socket
     */
    @Override
    public T get(String key, int hashCode)
            throws NoSuchElementException {
        if (!storageStructure.contains(key))
            throw new NoSuchElementException("Provided key does not mach any group");
        else if (!storageStructure.get(key).contains(hashCode))
            throw new NoSuchElementException("Provided hashCode does not match any socket");
        else
            return storageStructure.get(key).get(hashCode);
    }

    /**
     * this class does not available for this class
     *
     * @param key name of the socket
     * @throws IllegalAccessException always throw this error
     */
    @Override
    public void remove(String key)
            throws IllegalAccessException {
        throw new IllegalAccessException("This method is not available for group storage");
    }

    @Override
    public void remove(String key,
                       int hashCode)
            throws NoSuchElementException {
        if (!storageStructure.contains(key))    // check if the given key response to a group
            throw new NoSuchElementException("Provided key does not match any group");
        storageStructure.get(key).remove(hashCode); // remove the socket
        if (storageStructure.get(key).isEmpty())    // check the group which had a socket removed is empty
            storageStructure.remove(key);
    }

    @Override
    public void remove(T socket) {
        remove(socket.getKey(), socket.hashCode());
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }
}
