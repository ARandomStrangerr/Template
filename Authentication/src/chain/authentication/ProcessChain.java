package chain.authentication;

import chain.Chain;
import com.google.gson.JsonObject;

public class ProcessChain extends Chain {
    public ProcessChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new ProcessLinkGetPrivilege(this));
        chain.add(new ProcessLinkSendToMessageModule(this));
    }
}
