package chain.viettel_invoice_send;

import chain.Chain;
import com.google.gson.JsonObject;

import java.net.InetAddress;

public class InitChain extends Chain {
    private final InetAddress address;
    private final int port;

    public InitChain(InetAddress address,
                               int port) {
        super(null);
        this.address = address;
        this.port = port;
    }

    @Override
    protected void chainConstruction() {

    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }
}
