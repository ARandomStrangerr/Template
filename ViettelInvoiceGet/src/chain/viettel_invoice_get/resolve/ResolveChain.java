package chain.viettel_invoice_get.resolve;

import chain.Chain;
import chain.viettel_invoice_get.LinkSendToDataStream;
import com.google.gson.JsonObject;

public class ResolveChain extends Chain {
    public ResolveChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkGetInvoice(this));
        super.chain.add(new LinkSendToDataStream(this));
    }
}
