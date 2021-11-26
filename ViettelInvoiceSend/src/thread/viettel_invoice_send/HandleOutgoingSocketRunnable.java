package thread.viettel_invoice_send;

import chain.Chain;
import chain.viettel_invoice_send.ProcessChain;
import chain.viettel_invoice_send.RejectChain;
import chain.viettel_invoice_send.ResolveChain;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.MemorableViettelInvoiceSend;

import java.io.IOException;

public class HandleOutgoingSocketRunnable extends runnable.HandleOutgoingSocketRunnable<PlainSocket> {
    public HandleOutgoingSocketRunnable(PlainSocket socket) {
        super(socket);
    }

    @Override
    public boolean verification(PlainSocket socket) {
        try {
            socket.write(getModuleName());
        }catch (IOException e){
            System.err.println("Cannot write to the Message module");
            e.printStackTrace();
            return false;
        }
        try {
            MemorableViettelInvoiceSend.setHashCode(Integer.parseInt(socket.read()));
        } catch (IOException e){
            System.err.println("Cannot read from Message module");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e){
            System.err.println("The data send back is incorrect");
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
        return new ResolveChain(request);
    }

    /**
     * chain which runs after the request is failed to handle
     *
     * @param request the request to process
     * @return a Chain object to run
     */
    @Override
    protected Chain getRejectChain(JsonObject request) {
        return new RejectChain(request);
    }

    @Override
    protected String getModuleName() {
        return MemorableViettelInvoiceSend.getName();
    }
}
