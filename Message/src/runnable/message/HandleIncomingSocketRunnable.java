package runnable.message;

import chain.Chain;
import chain.message.ProcessChain;
import chain.message.ResolveChain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;

import java.io.IOException;

public class HandleIncomingSocketRunnable extends runnable.HandleIncomingSocketRunnable<PlainSocket> {
    private final PlainSocket socket;
    public HandleIncomingSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
        super(listener, socket);
        this.socket = socket;
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
        String data;
        try {
            data = socket.read();
        } catch (IOException e){
            System.err.println("cannot read the data from the incoming socket");
            e.printStackTrace();
            return false;
        }
        socket.setKey(data);
        return true;
    }

    /**
     * chain which process the request
     *
     * @param request the request to process
     * @return Chain object to run
     */
    @Override
    protected Chain getProcessChain(JsonObject request) {
        return new ProcessChain(request, socket);
    }

    /**
     * chain which runs after the request is successfully handled
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getResolveChain(JsonObject request) {
        return new ResolveChain(request);
    }

    /**
     * chain which runs after the request is failed to handle
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getRejectChain(JsonObject request){
        return new ResolveChain(request);
    }
}
