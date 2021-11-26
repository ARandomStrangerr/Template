package chain.viettel_invoice_send;

import chain.Link;
import memorable.MemorableViettelInvoiceSend;
import thread.viettel_invoice_send.HandleOutgoingSocketRunnable;

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
        new Thread(new HandleOutgoingSocketRunnable(MemorableViettelInvoiceSend.getOutgoingSocket())).start();
        return false;
    }
}
