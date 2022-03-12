package chain.data_stream.resolve;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import memorable.DataStream;
import socket.Socket;

import java.io.IOException;

public class SendPackage extends Link<ResolveChain> {
    public SendPackage(ResolveChain chain) {
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
        JsonObject header = chain.getProcessObject().get("header").getAsJsonObject();
        JsonArray to = header.get("to").getAsJsonArray();
        boolean status = header.get("status").getAsBoolean();
        Socket socket;
        if (to.size() == 0 || !status){ // case if the request run to its end or no longer valid
            String from = header.get("from").getAsString();
            int instance = header.get("instance").getAsInt();
            socket = DataStream.getInstance().listener.getSocket(from, instance);
        } else {
            socket = DataStream.getInstance().listener.getSocket(to.remove(0).getAsString());
        }
        try {
            socket.write(chain.getProcessObject().getAsString());
        } catch (IOException e){
            System.err.println("Cannot send package to indicated socket");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
