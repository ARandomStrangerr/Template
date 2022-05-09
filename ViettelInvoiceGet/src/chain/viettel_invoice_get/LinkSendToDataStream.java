package chain.viettel_invoice_get;

import chain.Chain;
import chain.Link;
import memorable.ViettelInvoiceGet;
import socket.Socket;

import java.io.IOException;

public class LinkSendToDataStream extends Link {
    public LinkSendToDataStream(Chain chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        /*
        in favour of update / memory complexity, put more stress on the CPU.
        every invoice that downloaded by this module became a standalone request itself and continue the chain in the previous step.
        this makes sending back info just to inform the DataStream module that this module is done with the instance of job and one less thread will be operated.
        (maybe inform the original module that the job is done also)
        one way or the other, the body message after this point is no longer needed.
        its only purposes is to inform decreasing counter
         */
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("decrease", false);
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("terminate", true);
        Socket socket;
        socket = ViettelInvoiceGet.getInstance().getSocket();
        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            socket.write(chain.getProcessObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
