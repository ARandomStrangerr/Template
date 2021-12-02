package chain.viettel_invoice_get;

import chain.Link;
import memorable.ViettelInvoiceGetMemorable;
import runnable.viettel_invoice_get.HandleOutgoingSocketRunnable;

public class InitLinkStartThreadForOutgoingSocket extends Link<InitChain> {
    public InitLinkStartThreadForOutgoingSocket(InitChain chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        new Thread(new HandleOutgoingSocketRunnable(ViettelInvoiceGetMemorable.getOutgoingConnection())).start();
        return true;
    }
}
