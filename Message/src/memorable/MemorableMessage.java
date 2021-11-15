package memorable;

import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;

public class MemorableMessage {
    private static Listener<PlainSocket> listener;
    private static String name;

    public static void setListener(Listener<PlainSocket> listener) {
        MemorableMessage.listener = listener;
    }

    public static Listener<PlainSocket> getListener() {
        return listener;
    }

    public static void setName(String name) {
        MemorableMessage.name = name;
    }

    public static String getName() {
        return name;
    }
}
