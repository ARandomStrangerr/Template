package chain.gmail_with_attachment.resolve;

import chain.Link;

public class LinkCheckSession extends Link<ResolveChain> {
    public LinkCheckSession(ResolveChain chain) {
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

        return false;
    }
}
