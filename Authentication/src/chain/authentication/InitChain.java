package chain.authentication;

import chain.Chain;
import memorable.AuthenticationMemorable;

public class InitChain extends Chain {
    private final String textFileName;
    private final String address;
    private final int port;
    public InitChain(String moduleName,
                     String textFileName,
                     String address,
                     int port) {
        super(null);
        this.textFileName = textFileName;
        AuthenticationMemorable.setName(moduleName);
        this.address = address;
        this.port = port;
    }

    @Override
    protected void chainConstruction() {
        super.chain.add(new InitLinkLoadTextFile(this));
        super.chain.add(new InitLinkStartAndStoreOutgoingSocket(this));
        super.chain.add(new InitLinkStartThreadForRunnable(this));
    }

    public String getTextFileName() {
        return textFileName;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
