package chain.incoming_connection;

import chain.Chain;
import chain.Link;
import memorable.IncomingConnection;

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
        try {
            IncomingConnection.getInstance().getListener().getSocket(clientId).write(chain.getProcessObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
