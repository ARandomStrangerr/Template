package memorable;

import socket.Socket;

public class ViettelInvoiceGet {
    private static ViettelInvoiceGet instance;

    public static ViettelInvoiceGet getInstance() {
        if (instance == null) instance = new ViettelInvoiceGet();
        return instance;
    }

    private String moduleName;
    private int instanceNum;
    private Socket socket;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getInstanceNum() {
        return instanceNum;
    }

    public void setInstanceNum(int instanceNum) {
        this.instanceNum = instanceNum;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
