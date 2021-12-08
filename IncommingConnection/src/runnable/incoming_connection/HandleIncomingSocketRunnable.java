package runnable.incoming_connection;

import chain.Chain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.IncomingConnectionMemorable;

public class HandleIncomingSocketRunnable extends runnable.HandleIncomingSocketRunnable<PlainSocket> {
    public HandleIncomingSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
        super(listener, socket);
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
