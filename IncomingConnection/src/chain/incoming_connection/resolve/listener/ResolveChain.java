package chain.incoming_connection.resolve.listener;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.IncomingConnection;

/**
 * chain responsible for process request comes from outside
 */
public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkReformatJson(this));
        chain.add(new LinkJobTranslate(this));
        chain.add(new LinkGetPrivilege(this, IncomingConnection.getInstance().getThreadTable()));
        chain.add(new LinkSendToDataStream(this));
    }
}
