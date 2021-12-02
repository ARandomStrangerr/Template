package chain.viettel_invoice_get;

import chain.Chain;
import com.google.gson.JsonObject;

public class ProcessChain extends Chain {
    public ProcessChain(JsonObject processObject) {
        super(processObject);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new ProcessLinkGetInvoice(this));
    }
}
