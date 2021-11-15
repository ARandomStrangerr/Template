package chain.message;

import chain.Chain;
import chain.Link;
import memorable.MemorableMessage;
import runnable.message.HandleListenerRunnable;

public class InitLinkStartThreadForListener extends Link {
    public InitLinkStartThreadForListener(Chain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        new Thread(new HandleListenerRunnable(MemorableMessage.getListener())).start();
        return true;
    }
}
