package chain.message;

import chain.Chain;
import chain.Link;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.listener.PlainListener;
import connection_and_storage.connection.socket.PlainSocket;
import connection_and_storage.storage.StorageType;
import memorable.MemorableMessage;

import java.io.IOException;

public class InitLinkStartAndStoreListener extends Link {
    private final int port;

    public InitLinkStartAndStoreListener(Chain chain,
                                         int port) {
        super(chain);
        this.port = port;
    }

    @Override
    protected boolean resolve() {
        PlainListener listener;
        try {
            listener = new PlainListener(port,
                    StorageType.GROUP);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        MemorableMessage.setListener(listener);
        return true;
    }
}
