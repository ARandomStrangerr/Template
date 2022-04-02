package chain.fake_authentication.resolve;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.FakeAuthentication;

import java.util.HashSet;

/**
 * check if client has authority to access the requested modules
 */
class LinkCheckPrivilege extends Link<ResolveChain> {
    LinkCheckPrivilege(ResolveChain chain) {
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
        JsonObject body = chain.getProcessObject().get("body").getAsJsonObject();
        String clientName = body.get("clientId").getAsString();
        JsonArray requestModules = body.get("requestModule").getAsJsonArray();
        try {
            HashSet<String> privilegeSet = FakeAuthentication.getInstance().getPrivilegeTable().get(clientName);
            for (JsonElement module : requestModules)
                if (!privilegeSet.contains(module.getAsString()))
                    throw new NullPointerException(); // will always throw exception in this line
        } catch (NullPointerException e) {
            chain.getProcessObject().addProperty("error", "client does not have privilege to access");
            System.err.println("client does not have privilege to access");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}