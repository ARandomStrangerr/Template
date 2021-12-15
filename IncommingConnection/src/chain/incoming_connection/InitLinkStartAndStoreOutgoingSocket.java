package chain.incoming_connection;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;
import runnable.incoming_connection.HandleOutgoingSocketRunnable;

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
        try{
            IncomingConnectionMemorable.setOutgoingSocket(
                    new PlainSocket(
                            chain.getOutgoingSocketAddress(),
                            chain.getOutgoingSocketPort()
                    )
            );
        }catch (IOException e){
            System.err.println(IncomingConnectionMemorable.getName() + " - Cannot open outgoing socket");
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }
}
