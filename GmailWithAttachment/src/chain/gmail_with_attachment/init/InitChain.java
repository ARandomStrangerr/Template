package chain.gmail_with_attachment.init;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    final InetAddress hostAddress;
    final int hostPort;
    final String name,
            clientId,
            clientSecret;

    public InitChain(InetAddress hostAddress,
                     int hostPort,
                     String name,
                     String clientId,
                     String clientSecret) {
        super(null);
        this.hostAddress = hostAddress;
        this.hostPort = hostPort;
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkStartSocket(this));
        chain.add(new LinkStoreVariable(this));
    }
}
