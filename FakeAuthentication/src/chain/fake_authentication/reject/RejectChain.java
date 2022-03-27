package chain.fake_authentication.reject;

import chain.Chain;
import chain.fake_authentication.LinkSendToDataStream;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkSendToDataStream(this));
    }
}
