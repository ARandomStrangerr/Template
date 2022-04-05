
import chain.incoming_connection.init.InitChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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

class TestViettelInvoiceGet {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 9999);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job","GetInvoice");
        jsonObject.addProperty("id", "3C:7C:3F:2A:07:61");
        jsonObject.addProperty("username", "0101183303-007");
        jsonObject.addProperty("password", "123456aA@");
        jsonObject.addProperty("templateCode", "02GTTT0/001");
        jsonObject.addProperty("invoiceSeries", "AA/20E");
        jsonObject.addProperty("start", 11037);
        jsonObject.addProperty("end", 11039);

        bw.write(jsonObject.toString());
        bw.newLine();
        bw.flush();

        Gson gson = new Gson();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            System.out.println(line);
            JsonObject inputObject = gson.fromJson(line, JsonObject.class);
            File file = new File("/home/thanhdo/Documents/" + inputObject.get("name").getAsString() + ".pdf");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(Base64.getDecoder().decode(inputObject.get("file").getAsString().getBytes(StandardCharsets.UTF_8)));
            fos.close();
        }
    }
}

class TestAuthentication {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String send = "{job: \"GetInvoice\", clientId:\"FC:34:97:C2:5A:2A\"}";
        bw.write(send);
        bw.newLine();
        bw.flush();

        System.out.println(br.readLine());
    }
}