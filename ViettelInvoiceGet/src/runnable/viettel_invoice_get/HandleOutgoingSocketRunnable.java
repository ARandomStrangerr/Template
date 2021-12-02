package runnable.viettel_invoice_get;

import chain.Chain;
import chain.viettel_invoice_get.ProcessChain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.ViettelInvoiceGetMemorable;

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
        try {
            socket.write(ViettelInvoiceGetMemorable.getName());
        } catch (IOException e) {
            System.err.println("Cannot write the name into socket");
            e.printStackTrace();
            return false;
        }
        try {
            ViettelInvoiceGetMemorable.setHashCode(Integer.parseInt(socket.read()));
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

    @Override
    protected String getModuleName() {
        return ViettelInvoiceGetMemorable.getName();
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
}
