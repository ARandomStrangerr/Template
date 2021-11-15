package connection_and_storage.connection.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PlainSocket extends SocketAbstract {
    public PlainSocket(InetAddress address,
                       int port)
            throws IOException {
        super(new Socket(address, port));
    }

    public PlainSocket(Socket socket)
            throws IOException {
        super(socket);
    }
}
