package memorable;

import socket.Socket;

public class BarcodeMaker {
    private static BarcodeMaker cache;

    public static BarcodeMaker getInstance() {
        if (cache == null) cache = new BarcodeMaker();
        return cache;
    }

    public Socket socket;
    public String moduleName;
    public int id;

    private BarcodeMaker(){}
}
