package chain.incoming_connection;

import chain.Link;
import memorable.IncomingConnectionMemorable;
import runnable.incoming_connection.HandleOutgoingSocketRunnable;

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
        new Thread(
                new HandleOutgoingSocketRunnable(
                        IncomingConnectionMemorable.getOutgoingSocket()
                )
        ).start();
        return true;
    }
}
