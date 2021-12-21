package chain.incoming_connection;

import chain.LinkObserver;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;
import java.util.ArrayList;

public class ProcessLinkIncomingSocketGetAuthorization extends LinkObserver<ProcessChainIncomingSocket> {
    public ProcessLinkIncomingSocketGetAuthorization(ProcessChainIncomingSocket chain) {
        super(chain,
                IncomingConnectionMemorable.getThreadStorage());
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        // setup header object to send to authentication module
        JsonObject headerObject = new JsonObject();
        headerObject.addProperty("from", IncomingConnectionMemorable.getName());
        headerObject.addProperty("hashCode", IncomingConnectionMemorable.getHashCode());
        headerObject.addProperty("threadCode", Thread.currentThread().hashCode());
        headerObject.addProperty("status", true);
        JsonArray toArray = new JsonArray();
        toArray.add("Authentication");
        headerObject.add("to", toArray);

        // setup body object to send to authentication module
        JsonObject bodyObject = new JsonObject();
        bodyObject.add("clientIdentity",
                chain.getProcessObject()
                        .get("header")
                        .getAsJsonObject()
                        .get("clientIdentity")
        );

        // setup object to send to authentication module
        JsonObject outputObject = new JsonObject();
        outputObject.add("header", headerObject);
        outputObject.add("body", bodyObject);
        try {
            IncomingConnectionMemorable.getOutgoingSocket().write(outputObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            synchronizedWait();
        } catch (Exception e) {
            System.err.println("Thread corrupted");
            return false;
        }

//        System.out.println(chain.getProcessObject());

        // after the thread will be resumed here
        // compare the difference between the requested modules and\
        for (JsonElement requestModule : chain.getProcessObject()
                .get("header")
                .getAsJsonObject()
                .get("to")
                .getAsJsonArray()
        ) {
            if (!additionalInfo
                    .get("body")
                    .getAsJsonObject()
                    .get("accessList")
                    .getAsJsonArray()
                    .contains(requestModule)
            ){
                bodyObject = new JsonObject();
                bodyObject.addProperty("response", "Không có quyền truy nhập");
                chain.getProcessObject()
                        .add("body", bodyObject);
                chain.getProcessObject()
                        .get("header")
                        .getAsJsonObject()
                        .addProperty("status", false);
                return false;
            }
        }

        return true;
    }
}
