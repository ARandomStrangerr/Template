package runnable;

import chain.Chain;
import com.google.gson.JsonObject;
import socket.Socket;


/**
 * this class designed as inline class. Just declare new as extends of {@link ListenerHandler}. the job of this class is
 * to provide the implementation of handling socket which is accepted during the process of {@link ListenerHandler}.
 * A class control the behaviour of {@link Socket} on the end which run {@link socket.Listener}.
 */
public abstract class HostSocketHandler implements Runnable {
    private final Socket socket;

    public HostSocketHandler(Socket socket) {
        this.socket = socket;
    }

    public abstract void run();

    /**
     * @return process do to when the process chain is successfully complete
     */
    public abstract Chain getResolveChain(JsonObject receivedObject);

    /**
     * @return process to do when the process chain is fail to complete
     */
    public abstract Chain getRejectChain(JsonObject receivedObject);

}
