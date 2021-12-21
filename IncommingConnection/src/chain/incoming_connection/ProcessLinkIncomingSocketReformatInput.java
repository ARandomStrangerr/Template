package chain.incoming_connection;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.IncomingConnectionMemorable;

import java.util.Map;

public class ProcessLinkIncomingSocketReformatInput extends Link<ProcessChainIncomingSocket> {
    public ProcessLinkIncomingSocketReformatInput(ProcessChainIncomingSocket chain) {
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
        // create the header object
        JsonObject headerObject = new JsonObject();
        headerObject.addProperty("from", IncomingConnectionMemorable.getName());
        headerObject.addProperty("hashCode", IncomingConnectionMemorable.getHashCode());
        headerObject.addProperty("clientIdentity",
                chain.getProcessObject()
                        .remove("id")
                        .getAsString()
        );
        headerObject.addProperty("status", true);
        JsonArray toArray = new JsonArray();
        switch (
                chain.getProcessObject()
                        .remove("job")
                        .getAsString()
        ) {
            case "Authentication":
                toArray.add("Authentication");
                headerObject.addProperty("status", false);
                break;
            case "CreateInvoice":
                toArray.add("ViettelInvoiceSend");
                break;
            case "GetInvoice":
                toArray.add("ViettelInvoiceGet");
                break;
            case "DeleteInvoice":
                toArray.add("ViettelInvoiceDelete");
                break;
            default:
                headerObject.addProperty("status", false);
                JsonObject bodyObject = new JsonObject();
                bodyObject.addProperty("response", "Việc không tồn tại");
                System.err.println(IncomingConnectionMemorable.getName() + " - the declared job does not exist");
                // todo modify for better flow of code later
                chain.getProcessObject().add("header", headerObject);
                chain.getProcessObject().add("body", bodyObject);
                return false;
        }
        headerObject.add("to", toArray);

        // set the body object
        JsonObject bodyObject = new JsonObject();
        for (Map.Entry<String, JsonElement> ele : chain.getProcessObject().entrySet()) {
            bodyObject.add(ele.getKey(), ele.getValue());
        }
        for (String key : bodyObject.keySet()){
            chain.getProcessObject().remove(key);
        }

        // add the header and body into the object
        chain.getProcessObject().add("header", headerObject);
        chain.getProcessObject().add("body", bodyObject);

        return true;
    }
}
