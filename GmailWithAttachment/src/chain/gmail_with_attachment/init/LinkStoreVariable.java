package chain.gmail_with_attachment.init;

import chain.Link;
import memorable.GmailWithAttachment;

class LinkStoreVariable extends Link<InitChain> {
    LinkStoreVariable(InitChain chain) {
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
        GmailWithAttachment.getInstance().moduleName = chain.name;
        GmailWithAttachment.getInstance().clientId = chain.clientId;
        GmailWithAttachment.getInstance().clientSecret = chain.clientSecret;
        return true;
    }
}
