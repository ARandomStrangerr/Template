package runnable.authentication;

import chain.Chain;
import chain.authentication.ProcessChain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.AuthenticationMemorable;

import java.io.IOException;

public class HandleOutgoingSocketRunnable extends runnable.HandleOutgoingSocketRunnable<PlainSocket> {
    public HandleOutgoingSocketRunnable(PlainSocket socket) {
        super(socket,
                null);
    }

    /**
     * step to verify sockets and get necessary information back from the host
     *
     * @param socket socket which we are operating on
     * @return <code>True</code> when successfully verify
     * <code>false</code> when unsuccessfully verify
     */
    @Override
    public boolean verification(PlainSocket socket) {
        try {
            socket.write(AuthenticationMemorable.getName());
        } catch (IOException e) {
            System.err.println(AuthenticationMemorable.getName() + " - cannot set name of the data module");
            e.printStackTrace();
            return false;
        }
        try {
            AuthenticationMemorable.setHashCode(Integer.parseInt(socket.read()));
        } catch (IOException e) {
            System.err.println(AuthenticationMemorable.getName() + " - cannot read hashcode from the message module");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.err.println(AuthenticationMemorable.getName() + " - cannot convert the send back hashcode to number");
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
        return new ProcessChain(request);
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
        return AuthenticationMemorable.getName();
    }
}
