package chain;

import memorable.IncomingConnectionMemorable;
import runnable.incoming_connection.HandleOutgoingSocketRunnable;

public class InitLinkStartOutgoingSocketThread extends Link<InitChain> {
    public InitLinkStartOutgoingSocketThread(InitChain chain) {
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
        new Thread(new HandleOutgoingSocketRunnable(IncomingConnectionMemorable.getOutgoingSocket())).start();
        return false;
    }
}
