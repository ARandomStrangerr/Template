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
        JsonArray to;
        try{
            to = header.get("to").getAsJsonArray();
        } catch (NullPointerException e){
            System.err.println("To array in the header is missing");
            e.printStackTrace();
            return false;
        }
        boolean status = header.get("status").getAsBoolean();
        // determine which socket to send to
        Socket socket;
        if (to.size() == 0 || !status) { // case if the request run to its end or no longer valid
            String from = header.get("from").getAsString();
            int instance = header.get("instance").getAsInt();
            socket = DataStream.getInstance().listener.getSocket(from, instance);
        } else {
            String socketGroupName = to.remove(0).getAsString();
            try {
                socket = DataStream.getInstance().listener.getSocket(socketGroupName);
            } catch (NullPointerException e) {
                System.err.printf("Socket group associated with the name %s does not exists", socketGroupName);
                chain.getProcessObject().addProperty("error", String.format("Socket under the name of %s does not exists, please contact programmer to get this problem fixed", socketGroupName));
                return false;
            }
        }
        // send package to the determinate socket
        try {
            socket.write(chain.getProcessObject().toString());
//            socket.increaseActiveRequest(); // increase the number of active request on the socket
        } catch (IOException e) {
            chain.getProcessObject().addProperty("error", "Cannot send package to indicated module");
            System.err.println("Cannot send package to indicated socket");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
