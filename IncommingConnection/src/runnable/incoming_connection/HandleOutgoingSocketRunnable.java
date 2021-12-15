package runnable.incoming_connection;

import chain.Chain;
import chain.incoming_connection.ProcessChainOutgoingSocket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

// todo later on upgrade this to ssh socket
public class HandleOutgoingSocketRunnable extends runnable.HandleOutgoingSocketRunnable<PlainSocket> {
    public HandleOutgoingSocketRunnable(PlainSocket socket) {
        super(socket);
    }

    @Override
    public void run(){
        // declare the name of this module to connect to the main module
        if (!verification(super.socket)){
            System.err.println(getModuleName() + " - Cannot verify the outgoing socket");
            try {
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            System.exit(1);
        }

        Gson gson = new Gson();
        while (true){
            String requestString;
            try {
                requestString = socket.read();
            } catch (IOException e){
                System.err.println(getModuleName() + " - Likely the other-side socket is closed");
                e.printStackTrace();
                break;
            }
            Runnable runnable = () -> {
                JsonObject requestObject = gson.fromJson(requestString, JsonObject.class);
                if (
                        requestObject
                        .get("header")
                        .getAsJsonObject()
                        .has("clientIdentity")
                ) {
                    getProcessChain(requestObject).resolve();
                } else if (
                        requestObject
                        .get("header")
                        .getAsJsonObject()
                        .has("threadCode")
                ) {
                    System.err.println("Not supported yet");
                }
            };
            new Thread (runnable).start();
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(getModuleName() + " - Fatal error, cannot close the socket after socket to main branch is broken");
            e.printStackTrace();
        }
        System.exit(1); // the outgoing socket does not work, proceed to close the program
    }

    /**
     * step to verify sockets and get necessary information back from the host
     *
     * @param socket socket which we are operating on
     * @return <code>True</code> when successfully verify
     * <code>false</code> when unsuccessfully verif
     */
    @Override
    public boolean verification(PlainSocket socket) {

        try{
            socket.write(IncomingConnectionMemorable.getName());
        } catch (IOException e){
            System.err.println(getModuleName() + " - Cannot write declare the name of this module to the message module");
            e.printStackTrace();
            return false;
        }

        try{
            IncomingConnectionMemorable.setHashCode(Integer.parseInt(socket.read()));
        } catch (IOException e){
            System.err.println(getModuleName() + " - Cannot read the hashcode of this module from the message module");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e){
            System.err.println(getModuleName() + " - Cannot convert the data from message module to number");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * chain which process the request
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getProcessChain(JsonObject request) {
        return new ProcessChainOutgoingSocket(request);
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
    protected String getModuleName() {
        return IncomingConnectionMemorable.getName();
    }
}
