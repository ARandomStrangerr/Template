package chain.incoming_connection;

import chain.Chain;
import memorable.IncomingConnectionMemorable;

import java.net.InetAddress;

public final class InitChain extends Chain {
    private final int listenerPort,
            outgoingSocketPort;
    private final InetAddress outgoingSocketAddress;

    public InitChain(String moduleName,
                     InetAddress outgoingSocketAddress,
                     int outgoingSocketPort,
                     int listenerPort) {
        super(null);
        IncomingConnectionMemorable.setName(moduleName);
        this.outgoingSocketAddress = outgoingSocketAddress;
        this.outgoingSocketPort = outgoingSocketPort;
        this.listenerPort = listenerPort;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new InitLinkStartAndStoreListener(this));
        super.chain.add(new InitLinkStartThreadForListener(this));
        super.chain.add(new InitLinkStartAndStoreOutgoingSocket(this));
        super.chain.add(new InitLinkStartThreadForOutgoingSocket(this));
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public int getOutgoingSocketPort() {
        return outgoingSocketPort;
    }

    public InetAddress getOutgoingSocketAddress() {
        return outgoingSocketAddress;
    }
}
