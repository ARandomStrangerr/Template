package chain.gmail_with_attachment.resolve;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import memorable.GmailWithAttachment;

public class LinkCreateAuthorization extends Link<ResolveChain> {
    public LinkCreateAuthorization(ResolveChain chain) {
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
        StringBuilder authenticationAddress = new StringBuilder();
        authenticationAddress.append("https://accounts.google.com/o/oauth2/v2/auth?")
                .append(String.format("client_id=%s", GmailWithAttachment.getInstance().clientId))
                .append(String.format("scope=%s", "https://www.googleapis.com/auth/gmail.compose"))
                .append(String.format("redirect_uri=%s"));

        JsonObject sendObj, headerObj, bodyObj;
        JsonArray toArr;

        toArr = new JsonArray();
        toArr.add("");

        headerObj = new JsonObject();
        headerObj.addProperty("from", GmailWithAttachment.getInstance().moduleName);
        headerObj.addProperty("instance", GmailWithAttachment.getInstance().moduleId);

        bodyObj = new JsonObject();
        bodyObj.addProperty("address", authenticationAddress.toString());

        sendObj = new JsonObject();
        sendObj.add("header", headerObj);
        sendObj.add("body", bodyObj);

        return true;
    }
}
