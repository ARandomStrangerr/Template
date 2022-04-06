package chain.viettel_invoice_get.reject;

import chain.Chain;
import chain.viettel_invoice_get.LinkSendToDataStream;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkSendToDataStream(this));
    }
}
