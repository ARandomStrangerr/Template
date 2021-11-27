package chain.viettel_invoice_send;

import chain.Chain;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new RejectChainCreateResponse(this));
        super.chain.add(new LinkCleanupJsonObject(this));
        super.chain.add(new LinkSendToMessageModule(this));
    }
}
