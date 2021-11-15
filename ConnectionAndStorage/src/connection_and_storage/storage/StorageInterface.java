package connection_and_storage.storage;

import connection_and_storage.connection.socket.SocketInterface;

import java.util.NoSuchElementException;

public interface StorageInterface<T extends SocketInterface> extends Iterable<T> {
    /**
     * add an instance of socket into this collection with its name
     *
     * @param key    name to store
     * @param socket the instance of the socket needed to be store
     */
    void put(String key,
             T socket);

    /**
     * get a socket based on its name.
     *
     * @param key name of the socket
     * @return socket associated with the name
     * @throws NoSuchElementException the name does not exist
     */
    T get(String key)
            throws NoSuchElementException;

    /**
     * get a socket specific based on its name and hash code
     *
     * @param key      name of the group
     * @param hashCode unique code of the socket
     * @return socket that associated with the name of the group
     * @throws NoSuchElementException when the given name or hashCode does not associate with anything
     * @throws IllegalAccessException this method only available for group storage
     */
    T get(String key,
          int hashCode)
            throws NoSuchElementException,
            IllegalAccessException;

    /**
     * remove a single socket based on its given name
     *
     * @param key name of the socket
     * @throws IllegalAccessException this method only available for single storage
     */
    void remove(String key)
            throws IllegalAccessException;

    /**
     * remove a single socket based on its given name and code
     *
     * @param key      name of the group
     * @param hashCode unique code of the socket
     * @throws NoSuchElementException when the given name or hashCode does not associate with anything
     * @throws IllegalAccessException this method only available for group storage
     */
    void remove(String key,
                int hashCode)
            throws NoSuchElementException,
            IllegalAccessException;

    /**
     * universal use for remove, depends on the class implement this class
     *
     * @param socket the socket needed to be removed
     * @throws NoSuchElementException
     */
    void remove(T socket)
            throws NoSuchElementException;
}
