package chain.incoming_connection.init;

import chain.Link;
import memorable.IncomingConnection;
import runnable.data_stream.ListenerHandler;
import socket.Listener;
import socket.StorageType;

import java.io.IOException;

class LinkStartListener extends Link<InitChain> {
    LinkStartListener(InitChain chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        // create listener
        Listener listener;
        try {
            listener = new Listener(chain.incomingConnectionPort, StorageType.ONE_TO_ONE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // store listener
        IncomingConnection.getInstance().setListener(listener);
        // start thread to handle listener
        new Thread(new ListenerHandler(listener, chain.initialDisconnectTime));
        return true;
    }
}
