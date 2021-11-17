package chain.message;

import chain.Link;
import com.google.gson.JsonArray;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.MemorableMessage;

public class ProcessLinkFindModuleAndSendRequest extends Link<ProcessChain> {
    public ProcessLinkFindModuleAndSendRequest(ProcessChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        JsonArray toJsonArray = chain.getProcessObject().get("header").getAsJsonObject()
                .get("to").getAsJsonArray();
        
        return true;
    }
}
