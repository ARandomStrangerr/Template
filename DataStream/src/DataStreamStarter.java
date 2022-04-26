import chain.data_stream.init_module.InitChain;

public class DataStreamStarter {
    public static void main(String[] args) {
        int port, timeout;
        try {
            port = Integer.parseInt(args[0]);
            timeout = Integer.parseInt(args[1]);
        } catch (IndexOutOfBoundsException e){
            System.err.println("missing passing in program argument");
            e.printStackTrace();
            return;
        } catch (NumberFormatException e){
            System.err.println("passing in number is in correct format");
            e.printStackTrace();
            return;
        }
        new InitChain(port, timeout).resolve();
    }
}
