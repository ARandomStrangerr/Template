import chain.fake_authentication.init.InitChain;

import java.net.InetAddress;

public class FakeAuthenticationStarter {
    public static void main(String[] args) throws Exception{
        new InitChain(InetAddress.getByName("localhost"),
                9999,
                "FakeAuthentication",
                "authenticate.txt")
                .resolve();
    }
}
