
import chain.incoming_connection.init.InitChain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class IncomingConnectionStarter {
    public static void main(String[] args) throws Exception{
        new InitChain(InetAddress.getByName("localhost"),
                9999,
                10000,
                "IncomingConnection",
                3000)
                .resolve();
    }
}

class Main1 {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        bw.write("UNKNOWN_MAC_ADDRESS");
        bw.newLine();
        bw.flush();

        String send = "{job: \"GetInvoice\"}";
        bw.write(send);
        bw.newLine();
        bw.flush();

        System.out.println(br.readLine());
    }
}