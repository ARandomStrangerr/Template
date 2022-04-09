package chain.incoming_connection;

import chain.Chain;
import chain.Link;
import memorable.IncomingConnection;
import socket.Socket;

import java.io.IOException;

/**
 * find the client based on client id then send data back to the client.
 */
public class LinkSendBackToClient extends Link {
    public LinkSendBackToClient(Chain chain) {
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
        String clientId = chain.getProcessObject().get("header").getAsJsonObject().get("clientId").getAsString();
        Socket socket = IncomingConnection.getInstance().getListener().getSocket(clientId);
        // send the processed file to socket
        String sendData = chain.getProcessObject().has("error") ? String.format("{\"error\":\"%s\"}",chain.getProcessObject().get("error")) : chain.getProcessObject().get("body").toString();
        try {
            socket.write(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
