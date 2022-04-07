package chain.viettel_invoice_send.init;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    final String name;
    final int port;
    final InetAddress address;

    public InitChain(InetAddress address,
                     String name,
                     int port) {
        super(null);
        this.name = name;
        this.port = port;
        this.address = address;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkOpenSocket(this));
    }
}
