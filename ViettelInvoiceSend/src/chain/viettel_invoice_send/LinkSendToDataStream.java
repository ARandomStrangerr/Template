package chain.viettel_invoice_send;

import chain.Chain;
import chain.Link;
import memorable.ViettelInvoiceSend;

import java.io.IOException;

public class LinkSendToDataStream extends Link {
    public LinkSendToDataStream(Chain chain) {
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
        try {
            ViettelInvoiceSend.getInstance().getSocket().write(chain.getProcessObject().toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
