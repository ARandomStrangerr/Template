package memorable;

import socket.Listener;

public class DataStream {
    private static DataStream cache;

    public static DataStream getInstance() {
        if (cache == null) cache = new DataStream();
        return cache;
    }

    /**
     * the listener which accept sockets
     */
    public Listener listener;
}
