package chain.incoming_connection.init;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    InetAddress dataStreamAddress;
    int dataStreamPort, incomingConnectionPort, initialDisconnectTime;
    String moduleName;

    public InitChain(InetAddress dataStreamAddress,
                     int dataStreamPort,
                     int incomingConnectionPort,
                     String moduleName,
                     int initialDisconnectTime) {
        super(null);
        this.dataStreamAddress = dataStreamAddress;
        this.dataStreamPort = dataStreamPort;
        this.incomingConnectionPort = incomingConnectionPort;
        this.moduleName = moduleName;
        this.initialDisconnectTime = initialDisconnectTime;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new LinkSetVariables(this));
        super.chain.add(new LinkStartSocket(this));
        super.chain.add(new LinkStartListener(this));
    }
}
