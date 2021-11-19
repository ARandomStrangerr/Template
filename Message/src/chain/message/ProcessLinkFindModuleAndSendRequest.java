package chain.message;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.MemorableMessage;

import java.io.IOException;

public class ProcessLinkFindModuleAndSendRequest extends Link<ProcessChain> {
    public ProcessLinkFindModuleAndSendRequest(ProcessChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        JsonObject headerObject = chain.getProcessObject().get("header").getAsJsonObject();
        JsonArray toJsonArray = headerObject.get("to").getAsJsonArray();
        boolean requestStatus = headerObject.get("status").getAsBoolean();
        // the request return to the original module when the error flag is raise or the chain of modules is completed
        Listener<PlainSocket> listener = MemorableMessage.getListener();
        PlainSocket receiveInfoSocket;
        if (toJsonArray.size() == 0 || !requestStatus) { // the module reach the end of its chain or already failed to resolve
            String fromModule = headerObject.get("from").getAsString();
            int hashCode = headerObject.get("hashCode").getAsInt();
            try {
                receiveInfoSocket = listener.get(fromModule, hashCode);
            } catch (IllegalAccessException e) {
                System.err.println("FATAL ERROR: Incorrect access storage structure, terminate immediately");
                e.printStackTrace();
                System.exit(1); // immediately terminate the program since it is using the wrong storing collection
                return false; // this wouldn't run, but it is required for the variable to be registered
            }
        } else { // the module still in the middle of the process and still valid
            String to = headerObject
                    .get("to").getAsJsonArray()
                    .remove(0).getAsString();
            receiveInfoSocket = listener.get(to);
        }
        //send the message to the indicated module
        try {
            receiveInfoSocket.write(chain.getProcessObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // increase the counter of the module
        receiveInfoSocket.increaseCounter();
        return true;
    }
}
