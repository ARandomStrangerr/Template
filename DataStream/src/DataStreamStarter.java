import chain.data_stream.init_module.InitChain;

public class DataStreamStarter {
    public static void main(String[] args) {
        new InitChain(9999, 3000).resolve();
    }
}
