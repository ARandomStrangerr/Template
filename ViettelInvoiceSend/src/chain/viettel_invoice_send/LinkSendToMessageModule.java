package chain.viettel_invoice_send;

import chain.Chain;
import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.MemorableViettelInvoiceSend;

import java.io.IOException;

public class LinkSendToMessageModule extends Link {
    public LinkSendToMessageModule(Chain chain) {
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
        PlainSocket outgoingSocket = MemorableViettelInvoiceSend.getOutgoingSocket();
        try {
            outgoingSocket.write(chain.getProcessObject().toString());
        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
