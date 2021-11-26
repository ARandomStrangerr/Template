package thread.viettel_invoice_delete;

import chain.Chain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.SocketInterface;

public class HandleOutgoingSocketRunnable extends runnable.HandleOutgoingSocketRunnable {
    public HandleOutgoingSocketRunnable(SocketInterface socket) {
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
    public boolean verification(SocketInterface socket) {
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
    protected String getModuleName() {
        return null;
    }
}
