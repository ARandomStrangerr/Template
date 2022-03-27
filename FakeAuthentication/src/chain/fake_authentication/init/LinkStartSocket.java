package chain.fake_authentication.init;

import chain.Link;
import memorable.FakeAuthentication;
import runnable.fake_authentication.ClientSocketHandler;
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
        try {
            socket = new Socket(chain.hostAddress, chain.port);
        }catch (IOException e){
            System.err.println("Cannot connect to DataStream");
            e.printStackTrace();
            return false;
        }
        FakeAuthentication.getInstance().setSocket(socket);
        new Thread(new ClientSocketHandler(socket, chain.moduleName)).start();
        return true;
    }
}
