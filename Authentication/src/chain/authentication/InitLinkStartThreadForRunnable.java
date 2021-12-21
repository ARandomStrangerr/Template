package chain.authentication;

import chain.Link;
import memorable.AuthenticationMemorable;
import runnable.authentication.HandleOutgoingSocketRunnable;

public class InitLinkStartThreadForRunnable extends Link<InitChain> {
    public InitLinkStartThreadForRunnable(InitChain chain) {
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
        new Thread(new HandleOutgoingSocketRunnable(AuthenticationMemorable.getOutgoingSocket())).start();
        return true;
    }
}
