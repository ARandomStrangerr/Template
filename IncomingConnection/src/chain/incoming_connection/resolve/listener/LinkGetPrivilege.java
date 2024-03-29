package chain.incoming_connection.resolve.listener;

import chain.Link;
import chain.PausedLinkStorage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import memorable.IncomingConnection;

import java.io.IOException;

/**
 * get access list of module for the client send the request
 */
class LinkGetPrivilege extends Link<ResolveChain> {
    LinkGetPrivilege(ResolveChain chain, PausedLinkStorage storage) {
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
        // craft outgoing package to send
        JsonObject outgoingPkg, header, body;
        JsonArray toArray;
        toArray = new JsonArray();
        toArray.add("FakeAuthentication");
        header = new JsonObject();
        header.addProperty("from", IncomingConnection.getInstance().getName());
        header.addProperty("instance", IncomingConnection.getInstance().getId());
        header.addProperty("thread", this.hashCode());
        header.add("to", toArray);
        header.addProperty("status", true);
        header.addProperty("decrease", false);
        body = new JsonObject();
        body.add("clientId", chain.getProcessObject().get("header").getAsJsonObject().get("clientId"));
        body.add("requestModule", chain.getProcessObject().get("header").getAsJsonObject().get("to"));
        outgoingPkg = new JsonObject();
        outgoingPkg.add("header", header);
        outgoingPkg.add("body", body);
        // send the package
        try {
            IncomingConnection.getInstance().getSocket().write(outgoingPkg.toString());
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        // pause this thread
        try {
            IncomingConnection.getInstance().getTable().pause(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        if(!this.additionalInfo.get("header").getAsJsonObject().get("status").getAsBoolean()){
            chain.getProcessObject().get("header").getAsJsonObject().addProperty("status", false);
            chain.getProcessObject().get("header").getAsJsonObject().addProperty("terminate", true);
            chain.getProcessObject().addProperty("error", "Người dùng không được uỷ quyền để sử dụng phần mềm");
            return false;
        }
        return true;
    }
}
