package chain.incoming_connection;

import chain.Link;
import memorable.IncomingConnectionMemorable;
import runnable.incoming_connection.HandleListenerRunnable;

public class InitLinkStartThreadForListener extends Link<InitChain> {
    public InitLinkStartThreadForListener(InitChain chain) {
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
        new Thread(new HandleListenerRunnable(IncomingConnectionMemorable.getListener())).start();
        return true;
    }
}
