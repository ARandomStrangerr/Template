package chain.viettel_invoice_get.init_module;

import chain.Link;
import memorable.ViettelInvoiceGet;
import runnable.viettel_invoice_get.ClientSocketHandler;
import socket.Socket;

import java.io.IOException;

/**
 *  connect socket to DataStream
 */
class LinkStartSocket extends Link<InitChain> {
    LinkStartSocket(InitChain chain) {
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
        Socket socket;
        try{ // start socket
            socket = new Socket(chain.address, chain.port);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        ViettelInvoiceGet.getInstance().setSocket(socket); // store socket
        ClientSocketHandler handler = new ClientSocketHandler(socket, chain.moduleName);
        new Thread(handler).start();
        System.out.printf("Opened socket to DataStrema at %s:%d\n", chain.address.getHostAddress(), chain.port);
        return true;
    }
}
