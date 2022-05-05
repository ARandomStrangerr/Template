import chain.fake_authentication.init.InitChain;

import java.net.InetAddress;

public class FakeAuthenticationStarter {
    public static void main(String[] args) throws Exception {
        InetAddress hostAddress = InetAddress.getByName(args[0]);
        int hostPort = Integer.parseInt(args[1]);
        String moduleName = args[2];
        String authenticationFilePath = args[3];
        new InitChain(hostAddress,
                hostPort,
                moduleName,
                authenticationFilePath)
                .resolve();
    }
}
