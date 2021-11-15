package chain.message;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.MemorableMessage;

public class InitChain extends Chain {

    public InitChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new InitLinkStartAndStoreListener(this, 1998));
        super.chain.add(new InitLinkStartThreadForListener(this));
    }
}
