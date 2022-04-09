package chain.incoming_connection.init;

import chain.Link;
import memorable.IncomingConnection;

import java.util.Hashtable;

class LinkSetVariables extends Link<InitChain> {
    public LinkSetVariables(InitChain chain) {
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
        IncomingConnection.getInstance().setName(chain.moduleName);
        IncomingConnection.getInstance().setThreadTable(new Hashtable<>());
        return true;
    }
}
