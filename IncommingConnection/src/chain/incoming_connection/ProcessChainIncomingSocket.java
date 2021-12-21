package chain.incoming_connection;

import chain.Chain;
import com.google.gson.JsonObject;

public class ProcessChainIncomingSocket extends Chain {
    public ProcessChainIncomingSocket(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new ProcessLinkIncomingSocketReformatInput(this));
        chain.add(new ProcessLinkIncomingSocketGetAuthorization(this));
    }
}
