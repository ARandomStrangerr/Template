package chain.incoming_connection;

import chain.Chain;
import chain.Link;
import memorable.IncomingConnection;
import socket.Socket;

import java.io.IOException;

public class LinkClientSocketTermination extends Link {
    public LinkClientSocketTermination(Chain chain) {
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
        boolean termination, status;
        try {
            termination = chain.getProcessObject().get("header").getAsJsonObject().get("terminate").getAsBoolean();
        } catch (NullPointerException e) {
            termination = false;
            System.err.println("Should have included TERMINATE=FALSE in the header");
        }
        status = chain.getProcessObject().get("header").getAsJsonObject().get("status").getAsBoolean();
        if (termination || !status) {
            String clientSocketName = chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString();
            Socket socket = IncomingConnection.getInstance().getListener().getSocket(clientSocketName);
            // close the socket manually
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // remove the socket from collection
            IncomingConnection.getInstance().getListener().removeSocket(socket);
            System.out.printf("Module is disconnected from the network %s - %d", socket.getName(), socket.hashCode());
        }
        return true;
    }
}
