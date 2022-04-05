package chain.incoming_connection.reject.listener;

import chain.Chain;
import chain.incoming_connection.LinkSendBackToClient;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkSendBackToClient(this));
    }
}
