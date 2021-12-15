package chain.incoming_connection;

import chain.Link;
import connection_and_storage.connection.listener.PlainListener;
import connection_and_storage.storage.StorageType;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public class InitLinkStartAndStoreListener extends Link<InitChain> {
    public InitLinkStartAndStoreListener(InitChain chain) {
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
        try {
            IncomingConnectionMemorable.setListener(
                    new PlainListener(
                            chain.getListenerPort(),
                            StorageType.SINGLE
                    )
            );
        } catch (IOException e) {
            System.err.println(IncomingConnectionMemorable.getName() + " - Cannot start listener");
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        return true;
    }
}
