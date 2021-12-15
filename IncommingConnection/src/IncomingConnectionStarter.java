import chain.incoming_connection.InitChain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class IncomingConnectionStarter {
    public static void main(String[] args) throws Exception{
        new InitChain(
                "IncomingConnection",
                InetAddress.getByName("localhost"),
                1998,
                1997
        ).resolve();
    }
}

class Main1 {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(InetAddress.getByName("localhost"), 1997);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        bw.write("{job:[\"TestModule\"], id:\"identification\", data:\"hello\"}");
        bw.newLine();
        bw.flush();

        System.out.println(br.readLine());
    }
}