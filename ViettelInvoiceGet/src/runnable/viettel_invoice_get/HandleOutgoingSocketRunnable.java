package runnable.viettel_invoice_get;

import chain.Chain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.listener.Listener;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.ViettelInvoiceMemorable;
import runnable.HandleIncomingSocketRunnable;

import java.io.IOException;

public class HandleOutgoingSocketRunnable extends HandleIncomingSocketRunnable<PlainSocket> {
    public HandleOutgoingSocketRunnable(Listener<PlainSocket> listener, PlainSocket socket) {
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
        try {
            socket.write(ViettelInvoiceMemorable.getName());
        } catch (IOException e) {
            System.err.println("Cannot write the name into socket");
            e.printStackTrace();
            return false;
        }
        try {
            ViettelInvoiceMemorable.setHashCode(Integer.parseInt(socket.read()));
        } catch (IOException e) {
            System.err.println("Cannot read the hash code from the message module");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.err.println("The send back id number is in correct");
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
}
