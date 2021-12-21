import chain.authentication.InitChain;

public class AuthenticationStater {
    public static void main(String[] args) {
        new InitChain("Authentication",
                "authenticate.txt",
                "localhost",
                1998).resolve();
    }
}
