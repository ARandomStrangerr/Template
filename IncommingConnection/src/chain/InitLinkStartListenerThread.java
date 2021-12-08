package chain;

import memorable.IncomingConnectionMemorable;
import runnable.incoming_connection.HandleListenerRunnable;

public final class InitLinkStartListenerThread extends Link<InitChain> {
    public InitLinkStartListenerThread(InitChain chain) {
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
