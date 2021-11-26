package chain.viettel_invoice_send;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;

import java.io.IOException;

public class InitLinkStartSocket extends Link<InitChain> {
    public InitLinkStartSocket(InitChain chain) {
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

        }
        return false;
    }
}
