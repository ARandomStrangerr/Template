package chain.viettel_invoice_get;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.ViettelInvoiceGetMemorable;

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
        // open outgoing socket
        PlainSocket outgoingConnection;
        try {
            outgoingConnection = new PlainSocket(chain.getAddress(), chain.getPort());
        } catch (IOException e) {
            System.err.println(ViettelInvoiceGetMemorable.getName() + " - cannot open socket to the main module");
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        // save outgoing socket
        ViettelInvoiceGetMemorable.setOutgoingConnection(outgoingConnection);
        return true;
    }
}
