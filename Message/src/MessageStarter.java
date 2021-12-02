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
        new InitChain(1998).resolve();
    }
}

class Class1 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 1998);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("Module1");
        bw.newLine();
        bw.flush();
        int assignedId = Integer.parseInt(br.readLine());
        JsonObject jsonObject = new JsonObject(),
                headerObject = new JsonObject(),
                bodyObject = new JsonObject();

        JsonArray toObject = new JsonArray();
        toObject.add("ViettelInvoiceGet");
        headerObject.addProperty("from", "Module1");
        headerObject.addProperty("hashCode", assignedId);
        headerObject.add("to", toObject);
        headerObject.addProperty("status", true);

        bodyObject.addProperty("username", "0101954482");
        bodyObject.addProperty("password", "123456aA@");
        bodyObject.addProperty("templateCode", "01GTKT0/001");
        bodyObject.addProperty("invoiceSeries", "KT/20E");
        bodyObject.addProperty("start", 69000);
        bodyObject.addProperty("end", 70000);

        jsonObject.add("header", headerObject);
        jsonObject.add("body", bodyObject);

        bw.write(jsonObject.toString());
        bw.newLine();
        bw.flush();

        Gson gson = new Gson();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            JsonObject inputObject = gson.fromJson(line, JsonObject.class);
            File file = new File("/home/thanhdo/test/" + inputObject.get("body").getAsJsonObject().get("name").getAsString());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(Base64.getDecoder().decode(inputObject.get("body").getAsJsonObject().get("file").getAsString().getBytes(StandardCharsets.UTF_8)));
            fos.close();
        }
    }
}

class Class2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 1998);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("Module2");
        bw.newLine();
        bw.flush();
        int assignedId = Integer.parseInt(br.readLine());
        String input = br.readLine();

        System.out.println(input);

        Gson gson = new Gson();
        JsonObject jsonInput = gson.fromJson(input, JsonObject.class);
        jsonInput.addProperty("prop2", "prop2");
        bw.write(jsonInput.toString());
        bw.newLine();
        bw.flush();
        Thread.sleep(1000000);
    }
}