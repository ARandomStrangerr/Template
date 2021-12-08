package runnable.incoming_connection;

import chain.Chain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

import java.io.IOException;

public class HandleOutgoingSocketRunnable extends runnable.HandleOutgoingSocketRunnable<PlainSocket> {
    public HandleOutgoingSocketRunnable(PlainSocket socket) {
        super(socket);
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
        // declare the name of this module
        try{
            socket.write("IncomingConnection");
        }catch (IOException e){
            System.err.println("Cannot write to the main module");
            e.printStackTrace();
            return false;
        }
        try{
            IncomingConnectionMemorable.setHashCode(Integer.parseInt(socket.read()));
        }catch (IOException e){
            System.err.println("Cannot read from outgoing socket");
            e.printStackTrace();
            return false;
        }catch (NumberFormatException e){
            System.err.println("Cannot convert the data into number");
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
    protected String getModuleName() {
        return null;
    }
}
