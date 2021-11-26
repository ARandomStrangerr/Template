package memorable;

import connection_and_storage.connection.socket.PlainSocket;

public class MemorableViettelInvoiceSend {
    private static String name;
    private static int hashCode;
    private static PlainSocket outgoingSocket;

    public static String getName(){
        return name;
    }

    public static void setName(String name){
        MemorableViettelInvoiceSend.name = name;
    }

    public static int getHashCode(){
        return hashCode;
    }

    public static void setHashCode(int hashCode){
        MemorableViettelInvoiceSend.hashCode = hashCode;
    }

    public static void setOutgoingSocket(PlainSocket outgoingSocket){
        MemorableViettelInvoiceSend.outgoingSocket = outgoingSocket;
    }

    public static PlainSocket getOutgoingSocket(){
        return MemorableViettelInvoiceSend.outgoingSocket;
    }
}
