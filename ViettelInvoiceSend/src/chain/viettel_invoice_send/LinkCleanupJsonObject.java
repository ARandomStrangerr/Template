package chain.viettel_invoice_send;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

public class LinkCleanupJsonObject extends Link {
    public LinkCleanupJsonObject(Chain chain) {
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
        JsonObject bodyObject = chain.getProcessObject().get("body").getAsJsonObject();
        bodyObject.remove("username");
        bodyObject.remove("password");
        bodyObject.remove("file");
        bodyObject.remove("send");
        bodyObject.remove("return");
        return true;
    }
}
