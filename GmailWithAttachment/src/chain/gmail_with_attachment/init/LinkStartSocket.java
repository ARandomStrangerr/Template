package chain.gmail_with_attachment.init;

import chain.Link;
import memorable.GmailWithAttachment;
import runnable.gmail_with_attachment.ClientSocketHandler;
import socket.Socket;

import java.io.IOException;

public class LinkStartSocket extends Link<InitChain> {
    public LinkStartSocket(InitChain chain) {
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
            socket = new Socket(chain.hostAddress, chain.hostPort);
        } catch (IOException e){
            System.err.println("Cannot connect to host");
            e.printStackTrace();
            return false;
        }
        GmailWithAttachment.getInstance().socketToDataStream =  socket;
        new Thread(new ClientSocketHandler(socket, chain.name)).start();
        return true;
    }
}
