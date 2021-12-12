package memorable;

import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import runnable.ThreadStorage;

public class IncomingConnectionMemorable {
    private static Listener<PlainSocket> listener;
    private static PlainSocket outgoingSocket;
    private static String name;
    private static int hashCode;
    private static ThreadStorage threadStorage;

    public static Listener<PlainSocket> getListener() {
        return listener;
    }

    public static void setListener(Listener<PlainSocket> listener) {
        IncomingConnectionMemorable.listener = listener;
    }

    public static PlainSocket getOutgoingSocket() {
        return outgoingSocket;
    }

    public static void setOutgoingSocket(PlainSocket outgoingSocket) {
        IncomingConnectionMemorable.outgoingSocket = outgoingSocket;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        IncomingConnectionMemorable.name = name;
    }

    public static int getHashCode() {
        return hashCode;
    }

    public static void setHashCode(int hashCode) {
        IncomingConnectionMemorable.hashCode = hashCode;
    }

    public static void setThreadStorage(ThreadStorage threadStorage){
        IncomingConnectionMemorable.threadStorage = threadStorage;
    }

    public static ThreadStorage getThreadStorage() {
        return threadStorage;
    }
}
