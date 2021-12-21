import chain.message.InitChain;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MessageStarter {
    public static void main(String[] args) throws Exception {
        new InitChain("Message",1998).resolve();
    }
}

class Class2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 1998);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("TestModule");
        bw.newLine();
        bw.flush();
        int assignedId = Integer.parseInt(br.readLine());
        String input = br.readLine();

        System.out.println(input);

        Gson gson = new Gson();
        JsonObject jsonInput = gson.fromJson(input, JsonObject.class);
        jsonInput.get("body").getAsJsonObject().addProperty("prop2", "prop2");

        bw.write(jsonInput.toString());
        bw.newLine();
        bw.flush();
    }
}