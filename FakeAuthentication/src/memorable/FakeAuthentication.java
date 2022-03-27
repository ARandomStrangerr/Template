package memorable;

import socket.Socket;

import java.util.HashSet;
import java.util.Hashtable;

public class FakeAuthentication{
    private static FakeAuthentication cache;

    public static FakeAuthentication getInstance() {
        if (cache == null) cache = new FakeAuthentication();
        return cache;
    }
    private String moduleName;
    private int instance;
    private Socket socket;
    private Hashtable<String, HashSet<String>> privilegeTable;
    private FakeAuthentication(){}

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public Hashtable<String, HashSet<String>> getPrivilegeTable() {
        return privilegeTable;
    }

    public void setPrivilegeTable(Hashtable<String, HashSet<String>> privilegeTable) {
        this.privilegeTable = privilegeTable;
    }
}
