package chain.incoming_connection.resolve.listener;

import chain.Link;
import com.google.gson.JsonObject;
import memorable.IncomingConnection;

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
        // separate and add property for the header of the object
        header = new JsonObject();
        header.addProperty("from", IncomingConnection.getInstance().getName());
        header.addProperty("instance", IncomingConnection.getInstance().getId());
        header.add("clientId", chain.getProcessObject().remove("clientId"));
        header.add("to", chain.getProcessObject().remove("to"));
        header.addProperty("status", false);
        header.addProperty("decrease", false);
        // separate and add property for the body of the object
        body = new JsonObject();
        for (String key : chain.getProcessObject().keySet()){
            body.add(key, chain.getProcessObject().get(key));
        }
        // wipe the data of the json object
        for (String key : chain.getProcessObject().keySet()){
            chain.getProcessObject().remove(key);
        }
        // add header and body into the object
        chain.getProcessObject().add("header", header);
        chain.getProcessObject().add("body", body);

        System.out.println(chain.getProcessObject());
        return true;
    }
}
