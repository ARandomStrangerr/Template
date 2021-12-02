package memorable;

import connection_and_storage.connection.socket.PlainSocket;

public class ViettelInvoiceGetMemorable {
    private static int hashCode;
    private static String name;
    private static PlainSocket outgoingConnection;

    public static int getHashCode() {
        return hashCode;
    }

    public static void setHashCode(int hashCode) {
        ViettelInvoiceGetMemorable.hashCode = hashCode;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ViettelInvoiceGetMemorable.name = name;
    }

    public static PlainSocket getOutgoingConnection() {
        return outgoingConnection;
    }

    public static void setOutgoingConnection(PlainSocket outgoingConnection) {
        ViettelInvoiceGetMemorable.outgoingConnection = outgoingConnection;
    }
}
