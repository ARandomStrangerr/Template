package chain.incoming_connection;

import chain.Link;
import memorable.IncomingConnectionMemorable;
import runnable.ThreadStorage;

public class InitLinkCreateAndStoreObserver extends Link<InitChain> {
    public InitLinkCreateAndStoreObserver(InitChain chain) {
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
        IncomingConnectionMemorable.setThreadStorage(new ThreadStorage());
        return true;
    }
}
