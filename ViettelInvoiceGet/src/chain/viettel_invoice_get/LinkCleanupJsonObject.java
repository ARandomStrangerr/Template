package chain.viettel_invoice_get;

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
        JsonObject object = chain.getProcessObject();
        object.remove("body");
        object.remove("header");
        JsonObject header = new JsonObject();
        header.addProperty("decreaseCounter", true);
        object.add("header", header);
        return false;
    }
}
