package memorable;

import connection_and_storage.connection.socket.PlainSocket;

import java.util.LinkedHashMap;
import java.util.List;

public class AuthenticationMemorable{
    private static LinkedHashMap<String, List<String>> authenticationMap;
    private static String name;
    private static int hashCode;
    private static PlainSocket outgoingSocket;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        AuthenticationMemorable.name = name;
    }

    public static int getHashCode() {
        return hashCode;
    }

    public static void setHashCode(int hashCode) {
        AuthenticationMemorable.hashCode = hashCode;
    }

    public static PlainSocket getOutgoingSocket() {
        return outgoingSocket;
    }

    public static void setOutgoingSocket(PlainSocket outgoingSocket) {
        AuthenticationMemorable.outgoingSocket = outgoingSocket;
    }

    public static LinkedHashMap<String, List<String>> getAuthenticationMap() {
        return authenticationMap;
    }

    public static void setAuthenticationMap(LinkedHashMap<String, List<String>> authenticationMap) {
        AuthenticationMemorable.authenticationMap = authenticationMap;
    }
}
