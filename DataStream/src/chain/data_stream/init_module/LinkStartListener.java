package chain.data_stream.init_module;

import chain.Link;
import memorable.DataStream;
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
        Listener listener;
        try {
            listener = new Listener(chain.port, StorageType.ONE_TO_MANY);
        } catch (IOException e) {
            System.err.println("Cannot open listener at the given port");
            return false;
        }
        DataStream.getInstance().listener = listener;
        ListenerHandler listenerRunnable = new ListenerHandler(listener, chain.timeout);
        System.out.printf("Listener is opened at port %d", chain.port);
        new Thread(listenerRunnable).start();
        return true;
    }
}
