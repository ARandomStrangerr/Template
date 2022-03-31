package chain.incoming_connection.resolve.listener;

import chain.Link;
import com.google.gson.JsonObject;

class LinkReformatJson extends Link<ResolveChain> {
    LinkReformatJson(ResolveChain chain) {
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
        JsonObject header, body;
        header = new JsonObject();
        body = new JsonObject();
        return false;
    }
}
