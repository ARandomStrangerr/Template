package chain.viettel_invoice_send;

import chain.Chain;
import com.google.gson.JsonObject;

public class RejectChain extends Chain {
    public RejectChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {

    }
}
