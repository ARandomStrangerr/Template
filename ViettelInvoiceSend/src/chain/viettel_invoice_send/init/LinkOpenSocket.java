package chain.viettel_invoice_send.init;

import chain.Link;
import memorable.ViettelInvoiceSend;
import runnable.viettel_invoice_send.ClientSocketHandler;
import socket.Socket;

import java.io.IOException;

class LinkOpenSocket extends Link<InitChain> {
     LinkOpenSocket(InitChain chain) {
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
        try{
            socket = new Socket(chain.address, chain.port);
        } catch (IOException e){
            System.err.println("Cannot open connection to DataStream");
            e.printStackTrace();
            return false;
        }
        ViettelInvoiceSend.getInstance().setSocket(socket);
        Runnable runnable = new ClientSocketHandler(socket, chain.name);
        new Thread(runnable).start();
        System.out.printf("Success connect to DataStream at %s:%d", chain.address.getHostAddress(), chain.port);
        return false;
    }
}
