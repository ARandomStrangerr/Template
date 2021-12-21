package chain.authentication;

import chain.Link;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.AuthenticationMemorable;

import java.io.IOException;
import java.net.InetAddress;

public class InitLinkStartAndStoreOutgoingSocket extends Link<InitChain> {
    public InitLinkStartAndStoreOutgoingSocket(InitChain chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        try {
            AuthenticationMemorable.setOutgoingSocket(
                    new PlainSocket(
                            InetAddress.getByName(chain.getAddress()),
                            chain.getPort()
                    )
            );
        } catch (IOException e) {
            System.err.println(AuthenticationMemorable.getName() + " - Cannot open connection to the message module");
            e.printStackTrace();
            System.exit(1);
            return false;
        }
        return true;
    }
}
