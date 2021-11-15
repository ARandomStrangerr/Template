package connection_and_storage.connection.listener;

import connection_and_storage.connection.socket.PlainSocket;
import connection_and_storage.storage.StorageType;

import java.io.IOException;

public class PlainListener extends Listener<PlainSocket> {

    public PlainListener(int port, StorageType type) throws IOException {
        super(port, type);
    }

    /**
     * accept an incoming connection
     *
     * @return an established socket with by this listener
     * @throws IOException when fail to establish the socket
     */
    @Override
    public PlainSocket accept() throws IOException {
        return new PlainSocket(acceptSocket());
    }
}
