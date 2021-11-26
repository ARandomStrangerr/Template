import chain.message.InitChain;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageStarter {
    public static void main(String[] args) throws Exception {
        new InitChain(1998).resolve();
    }
}

class TestJsonNull{
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("prop1", null);

        System.out.println(jsonObject.get("prop1").isJsonNull());
    }
}

class TestStarter {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(InetAddress.getByName("localhost"), 1998);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw.write("RECEIVE_MODULE");
        bw.newLine();
        bw.flush();
        int assignedHashCode = Integer.parseInt(br.readLine());
        System.out.println(assignedHashCode);
        System.out.println(br.readLine());
    }
}

class TestStarter2{
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(InetAddress.getByName("localhost"),1998);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("SEND_MODULE");
        bw.newLine();
        bw.flush();
        int assignedHashCode = Integer.parseInt(br.readLine());
        System.out.println(assignedHashCode);
        JsonObject object = new JsonObject(),
                headerObject = new JsonObject();
        headerObject.addProperty("from","SEND_MODULE");
        headerObject.addProperty("hashCode", assignedHashCode);
        headerObject.addProperty("status", true);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("RECEIVE_MODULE");
        headerObject.add("to", jsonArray);

        object.add("header", headerObject);

        bw.write(object.toString());
        bw.newLine();
        bw.flush();

        Thread.sleep(10000);
    }
}
