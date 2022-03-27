package chain.fake_authentication.init;

import chain.Chain;

import java.net.InetAddress;

public class InitChain extends Chain {
    final InetAddress hostAddress;
    final int port;
    final String moduleName, authenticationFile;

    public InitChain(InetAddress hostAddress, int port, String moduleName, String authenticationFile) {
        super(null);
        this.hostAddress = hostAddress;
        this.port = port;
        this.moduleName = moduleName;
        this.authenticationFile = authenticationFile;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new ReadFile(this));
        super.chain.add(new LinkStartSocket(this));
    }
}
