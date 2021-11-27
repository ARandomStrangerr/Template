package memorable;

import connection_and_storage.connection.socket.PlainSocket;

public class ViettelInvoiceMemorable {
    private static int hashCode;
    private static String name;
    private static PlainSocket outgoingConnection;

    public static int getHashCode() {
        return hashCode;
    }

    public static void setHashCode(int hashCode) {
        ViettelInvoiceMemorable.hashCode = hashCode;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ViettelInvoiceMemorable.name = name;
    }

    public static PlainSocket getOutgoingConnection() {
        return outgoingConnection;
    }

    public static void setOutgoingConnection(PlainSocket outgoingConnection) {
        ViettelInvoiceMemorable.outgoingConnection = outgoingConnection;
    }
}
