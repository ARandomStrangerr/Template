package chain.fake_authentication;

import chain.Chain;
import chain.Link;
import memorable.FakeAuthentication;
import socket.Socket;

import java.io.IOException;

/**
 * send json after process back to DataStream
 */
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
        chain.getProcessObject().get("header").getAsJsonObject().addProperty("decrease", true);
        Socket socket = FakeAuthentication.getInstance().getSocket();
        try{
            socket.write(chain.getProcessObject().toString());
        } catch (IOException e){
            System.err.println("Cannot write to data stream");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
