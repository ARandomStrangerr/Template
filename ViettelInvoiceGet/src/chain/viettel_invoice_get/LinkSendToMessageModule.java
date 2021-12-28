package chain.viettel_invoice_get;

import chain.Chain;
import chain.Link;
import memorable.ViettelInvoiceGetMemorable;

import java.io.IOException;

public class LinkSendToMessageModule extends Link {
    public LinkSendToMessageModule(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        try {
            ViettelInvoiceGetMemorable.getOutgoingConnection().write(chain.getProcessObject().toString());
        } catch (IOException e){
            System.err.println("Cannot write to the outgoing socket");
            e.printStackTrace();
        }
        return true;
    }
}
