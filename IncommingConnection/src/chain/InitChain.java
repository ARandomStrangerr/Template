package chain;

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
        chain.add(new InitLinkStartAndStoreListener(this));
        chain.add(new InitLinkStartAndStoreOutgoingSocket(this));
        chain.add(new InitLinkStartListenerThread(this));
        chain.add(new InitLinkStartOutgoingSocketThread(this));
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
