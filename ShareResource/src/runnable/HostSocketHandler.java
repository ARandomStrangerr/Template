package runnable;

import chain.Chain;
import com.google.gson.JsonObject;
import socket.Socket;


/**
 * this class designed to be created as inline class.
 * that will ensure the access to the private member socket.
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
