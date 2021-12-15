package runnable.incoming_connection;

import chain.Chain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public class HandleIncomingSocketRunnable extends runnable.HandleIncomingSocketRunnable<PlainSocket> {
    public HandleIncomingSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
        super(listener, socket);
    }

    @Override
    public void run() {
        String inputMsg;
        try {
            inputMsg = socket.read();
        } catch (IOException e) {
            System.err.println(getName() + " - Cannot read from the input stream");
            e.printStackTrace();
            try {
                socket.write("{response:\"Không thể đọc dữ liệu gửi đến\"}");
                socket.close();
            }catch (IOException e1){
                e1.printStackTrace();
            }
            return;
        }

        JsonObject inputObject;
        Gson gson = new Gson();
        try {
            inputObject = gson.fromJson(inputMsg, JsonObject.class);
        } catch (Exception e){
            System.err.println(getName() + " - not incorrect Json format");
            e.printStackTrace();
            try {
                socket.write("{response:\"Định dạng dữ liệu được gởi đến không chính xác\"}");
                socket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
            return;
        }

        //todo: add authentication at this step after the demo stabilize

        socket.setKey(inputObject.remove("id").getAsString());
        JsonObject header = new JsonObject();
        header.addProperty("from", getName());
        header.add("to", inputObject.remove("job"));
        header.addProperty("hashCode", IncomingConnectionMemorable.getHashCode());
        header.addProperty("clientIdentity", socket.getKey());
        header.addProperty("status", true);
        JsonObject outputObject = new JsonObject();
        outputObject.add("header", header);
        outputObject.add("body", inputObject);

//        System.out.println(outputObject);
        try {
            IncomingConnectionMemorable.getOutgoingSocket().write(outputObject.toString());
        }catch (IOException e){
            System.err.println(getName() + " - Cannot write the messsage to ");
        }

        IncomingConnectionMemorable.getListener().put(socket.getKey(), socket);
    }

    /**
     * verify the socket
     *
     * @param socket the instance that being verified
     * @return true when the socket pass the verification
     * false when the socket does not pass the verification
     */
    @Override
    protected boolean verificationAndSetKeySocket(PlainSocket socket) {
        return false;
    }

    /**
     * chain which process the request
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getProcessChain(JsonObject request) {
        return null;
    }

    /**
     * chain which runs after the request is successfully handled
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getResolveChain(JsonObject request) {
        return null;
    }

    /**
     * chain which runs after the request is failed to handle
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getRejectChain(JsonObject request) {
        return null;
    }

    @Override
    protected String getName() {
        return IncomingConnectionMemorable.getName();
    }
}
