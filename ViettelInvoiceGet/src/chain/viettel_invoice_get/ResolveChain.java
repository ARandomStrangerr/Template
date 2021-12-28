package chain.viettel_invoice_get;

import chain.Chain;
import com.google.gson.JsonObject;

public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new ResolveLinkMakeReturnMessage(this));
        chain.add(new LinkSendToMessageModule(this));
    }
}
