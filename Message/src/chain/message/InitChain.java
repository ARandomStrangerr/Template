package chain.message;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.MemorableMessage;

public final class InitChain extends Chain {
    private final int port;

    public InitChain(String moduleName,
                     int port) {
        super(null);
        MemorableMessage.setName(moduleName);
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
