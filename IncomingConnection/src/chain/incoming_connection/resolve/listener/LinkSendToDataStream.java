package chain.incoming_connection.resolve.listener;

import chain.Link;
import memorable.IncomingConnection;

import java.io.IOException;

/**
 * send this batch of data to DataStream
 */
public class LinkSendToDataStream extends Link<ResolveChain> {
    public LinkSendToDataStream(ResolveChain chain) {
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
        try {
            IncomingConnection.getInstance().getSocket().write(chain.getProcessObject().toString());
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
