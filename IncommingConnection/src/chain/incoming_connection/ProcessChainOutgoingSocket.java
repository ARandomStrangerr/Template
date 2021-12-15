package chain.incoming_connection;

import chain.Chain;
import com.google.gson.JsonObject;

public class ProcessChainOutgoingSocket extends Chain {
    public ProcessChainOutgoingSocket(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new ProcessLinkOutgoingSocketWriteToClient(this));
        super.chain.add(new ProcessLinkOutgoingSocketCloseSocket(this));
    }
}
