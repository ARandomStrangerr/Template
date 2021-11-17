package chain.message;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.MemorableMessage;

public final class InitChain extends Chain {
    private final int port;

    public InitChain(int port) {
        super(null);
        this.port = port;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new InitLinkStartAndStoreListener(this));
        super.chain.add(new InitLinkStartThreadForListener(this));
    }

    int getPort() {
        return port;
    }
}
