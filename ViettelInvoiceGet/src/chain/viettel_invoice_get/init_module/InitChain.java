package chain.viettel_invoice_get.init_module;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    final InetAddress address;
    final int port;
    final String moduleName;

    public InitChain(InetAddress address, int port, String moduleName) {
        super(null);
        this.address = address;
        this.port = port;
        this.moduleName = moduleName;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkStartSocket(this));
    }
}
