package socket;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class OneToManyStorage implements StorageInterface {
    ConcurrentHashMap<String, ConcurrentHashMap<Integer, Socket>> storageStructure;

    OneToManyStorage() {
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
     * @throws NullPointerException one-to-one: when there is no socket is associated with the given key.
     *                              one-to-many: when there is no group response to such key
     */
    @Override
    public Socket get(String key) throws NullPointerException {
        /*
        todo figure out the best structure to do this method
        would like to achieve fast get / put time on getting socket by its id
        would like to achieve fast get time for socket with smallest actives instances
        currently doing the linear search
         */
        ConcurrentHashMap<Integer, Socket> group = storageStructure.get(key);
        if (group == null) throw new NullPointerException("No group response to such key");
        Socket socket = null;
        for (Map.Entry<Integer, Socket> entry : group.entrySet()) {
            if (entry.getValue().getActiveRequest() == 0) return entry.getValue();
            else if (socket == null) {
                socket = entry.getValue();
            } else if (socket != null) {
                if (entry.getValue().getActiveRequest() < socket.getActiveRequest()) {
                    socket = entry.getValue();
                }
            }
        }
        return socket;
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
     * @throws NullPointerException     when no group associated with the given key or no socket associated with the id
     */
    @Override
    public Socket get(String key, int id) throws IllegalAccessException {
        ConcurrentHashMap<Integer, Socket> group = storageStructure.get(key);
        if (group == null) throw new NullPointerException("cannot find anything associated with the group name");
        Socket socket = group.get(id);
        if (socket == null) throw new NullPointerException("cannot find anything associated with the id of the socket");
        return socket;
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
        ConcurrentHashMap<Integer, Socket> group = storageStructure.get(key);
        if (group == null) group = new ConcurrentHashMap<>();
        group.put(socket.hashCode(), socket);
    }

    /**
     * only available for one-to-one storage
     * remove the socket with the associated name
     *
     * @param key name of the socket
     * @return removed socket
     * @throws NullPointerException   when there is no such socket to remove
     * @throws IllegalAccessException when this method is tried to be accessed by one-to-many storage
     */
    @Override
    public Socket remove(String key) throws NullPointerException, IllegalAccessException {
        throw new IllegalAccessException("This method is not available for one-to-many storage");
    }

    /**
     * only available for one-to-many storage
     * remove the socket within the group name with associated id
     *
     * @param key group that contains the socket
     * @param id  unique id of the socket
     * @throws NullPointerException   when there is no such socket to remove
     * @throws IllegalAccessException when this method is tried to be accessed by one-to-one storage
     */
    @Override
    public Socket remove(String key, int id) throws NullPointerException, IllegalAccessException {
        ConcurrentHashMap<Integer, Socket> group = storageStructure.get(key);
        if (group == null) throw new NullPointerException("No group associated with given key");
        Socket socket = group.remove(id);
        if (socket == null) throw new NullPointerException("No such socket with the id in the group to remove");
        return socket;
    }

    public Socket remove(Socket socket) {
        try {
            return this.remove(socket.getName(), socket.hashCode());
        } catch (IllegalAccessException e) {
            System.err.println("this is literally impossible");
        }
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Socket> iterator() {
        LinkedList<Socket> itrList = new LinkedList<>();
        for (Map.Entry<String, ConcurrentHashMap<Integer, Socket>> entry : storageStructure.entrySet()) {
            for (Map.Entry<Integer, Socket> socket : entry.getValue().entrySet()) {
                itrList.add(socket.getValue());
            }
        }
        return itrList.iterator();
    }
}
