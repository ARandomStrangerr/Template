package chain.message;

import chain.Chain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;

public final class ProcessChain extends Chain {
    private final PlainSocket plainSocket;
    public ProcessChain(JsonObject processObject,
                        PlainSocket plainSocket) {
        super(processObject);
        this.plainSocket = plainSocket;
    }

    @Override
    protected void chainConstruction() {
        chain.add(new ProcessLinkDecreaseCounter(this));
    }

    PlainSocket getPlainSocket(){
        return plainSocket;
    }
}
