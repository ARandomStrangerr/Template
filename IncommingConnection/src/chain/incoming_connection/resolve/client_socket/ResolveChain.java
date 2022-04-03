package chain.incoming_connection.resolve.client_socket;

import chain.Chain;
import chain.incoming_connection.LinkSendBackToClient;
import com.google.gson.JsonObject;

/**
 * chain responsible for process request packages come from DataStream module
 */
public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkProvokeThread(this));
        super.chain.add(new LinkSendBackToClient(this));
    }
}
