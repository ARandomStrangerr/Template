package chain.message;

import chain.Link;
import memorable.MemorableMessage;
import runnable.message.HandleListenerRunnable;

public final class InitLinkStartThreadForListener extends Link<InitChain> {
    public InitLinkStartThreadForListener(InitChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        new Thread(new HandleListenerRunnable(MemorableMessage.getListener())).start();
        return true;
    }
}
