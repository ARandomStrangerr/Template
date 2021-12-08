package chain;

import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public final class InitLinkStartAndStoreOutgoingSocket extends Link<InitChain> {
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
        PlainSocket outgoingSocket;
        try {
            outgoingSocket = new PlainSocket(chain.getOutgoingSocketAddress(),
                    chain.getOutgoingSocketPort());
        } catch (IOException e) {
            System.err.println(IncomingConnectionMemorable.getName() + " - fail to open outgoing connection");
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        IncomingConnectionMemorable.setOutgoingSocket(outgoingSocket);
        return true;
    }
}
