package chain.viettel_invoice_send;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.MemorableViettelInvoiceSend;

import java.io.IOException;

public class InitLinkStartAndStoreOutgoingSocket extends Link<InitChain> {
    public InitLinkStartAndStoreOutgoingSocket(InitChain chain) {
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
        PlainSocket socket;
        try {
            socket = new PlainSocket(chain.getAddress(), chain.getPort());
        } catch (IOException e) {
            System.err.println("Cannot initiate connection with the message module");
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        MemorableViettelInvoiceSend.setOutgoingSocket(socket);
        return true;
    }
}
