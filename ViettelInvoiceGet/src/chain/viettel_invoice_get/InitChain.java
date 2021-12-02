package chain.viettel_invoice_get;

import chain.Chain;
import memorable.ViettelInvoiceGetMemorable;

import java.net.InetAddress;

public class InitChain extends Chain {
    private final InetAddress address;
    private final int port;

    public InitChain(String name,
                     InetAddress messageModuleAddress,
                     int port) {
        super(null);
        ViettelInvoiceGetMemorable.setName(name);
        this.address = messageModuleAddress;
        this.port = port;
    }

    @Override
    protected void chainConstruction() {
        chain.add(new InitLinkOpenAndSaveOutgoingSocket(this));
        chain.add(new InitLinkStartThreadForOutgoingSocket(this));
    }

    InetAddress getAddress() {
        return address;
    }

    int getPort() {
        return port;
    }
}
