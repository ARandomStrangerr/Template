package chain.data_stream.reject;

import chain.Link;
import memorable.DataStream;
import socket.Socket;

import java.io.IOException;

class SendPackage extends Link<RejectChain> {
    public SendPackage(RejectChain chain) {
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
        String fromModule;
        fromModule = chain.getProcessObject().get("header").getAsJsonObject().get("from").getAsString();
        int instance;
        instance = chain.getProcessObject().get("header").getAsJsonObject().get("instance").getAsInt();
        Socket socket;
        socket = DataStream.getInstance().listener.getSocket(fromModule, instance);
        try {
            socket.write(chain.getProcessObject().toString());
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
