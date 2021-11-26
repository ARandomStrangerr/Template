package chain.viettel_invoice_send;

import chain.Chain;
import com.google.gson.JsonObject;
import memorable.MemorableViettelInvoiceSend;

import java.net.InetAddress;

public class InitChain extends Chain {
    private final InetAddress address;
    private final int port;

    public InitChain(String moduleName,
                     InetAddress address,
                     int port) {
        super(null);
        this.address = address;
        this.port = port;
        MemorableViettelInvoiceSend.setName(moduleName);
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new InitLinkStartAndStoreOutgoingSocket(this));
        super.chain.add(new InitLinkStartThreadForOutgoingSocket(this));
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }
}
