import chain.incoming_connection.InitChain;

import java.net.InetAddress;

public class IncomingConnectionStarter {
    public static void main(String[] args) throws Exception{
        new InitChain("IncomingConnection",
                InetAddress.getByName("localhost"),
                1998,
                1997)
                .resolve();
    }
}
