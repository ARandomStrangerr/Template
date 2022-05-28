package chain.barcode_maker.init;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    final InetAddress hostAddress;
    final int hostPort;
    final String moduleName;

    public InitChain(InetAddress hostAddress,
                     int hostPort,
                     String moduleName) {
        super(null);
        this.hostAddress = hostAddress;
        this.hostPort = hostPort;
        this.moduleName = moduleName;
    }

    @Override
    protected void chainConstruction() {
        chain.add(new LinkStartListener(this));
    }
}
