package chain.message;

import chain.Link;
import connection_and_storage.connection.listener.PlainListener;
import connection_and_storage.storage.StorageType;
import memorable.MemorableMessage;

import java.io.IOException;

public final class InitLinkStartAndStoreListener extends Link<InitChain> {
    public InitLinkStartAndStoreListener(InitChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        PlainListener listener;
        try {
            listener = new PlainListener(chain.getPort(),
                    StorageType.GROUP);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        MemorableMessage.setListener(listener);
        return true;
    }
}
