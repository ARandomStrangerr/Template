package memorable;

import chain.PausedLinkTable;
import socket.Listener;
import socket.Socket;

import java.util.Hashtable;

public class IncomingConnection {
    private static IncomingConnection cache;

    public static IncomingConnection getInstance() {
        if (cache == null) cache = new IncomingConnection();
        return cache;
    }

    private Listener listener;
    private Socket socket;
    private int id;
    private String name;
    private PausedLinkTable table;

    private IncomingConnection() {
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PausedLinkTable getTable() {
        return table;
    }

    public void setTable(PausedLinkTable table) {
        this.table = table;
    }
}
