package memorable;

import socket.Socket;

public class ViettelInvoiceSend {
    private static ViettelInvoiceSend cache;

    public static ViettelInvoiceSend getInstance() {
        if (cache == null) cache = new ViettelInvoiceSend();
        return cache;
    }

    private String name;
    private int id;
    private Socket socket;
    private ViettelInvoiceSend(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
