package chain.fake_authentication.resolve;

import chain.Chain;
import chain.fake_authentication.LinkSendToDataStream;
import com.google.gson.JsonObject;

public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkCheckPrivilege(this));
        super.chain.add(new LinkSendToDataStream(this));
    }
}
