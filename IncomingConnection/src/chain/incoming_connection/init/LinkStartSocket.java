package chain.incoming_connection.init;

import chain.Link;
import memorable.IncomingConnection;
import runnable.incoming_connection.ClientSocketHandler;
import socket.Socket;

import java.io.IOException;

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
        // start socket
        Socket  socket;
        try{
            socket = new Socket(chain.dataStreamAddress, chain.dataStreamPort);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        // store the socket
        IncomingConnection.getInstance().setSocket(socket);
        // start thread to handle socket
        new Thread(new ClientSocketHandler(socket, IncomingConnection.getInstance().getName())).start();
        System.out.printf("Connected to DataStream at %s:%d\n", chain.dataStreamAddress.getHostAddress(), chain.dataStreamPort);
        return true;
    }
}
