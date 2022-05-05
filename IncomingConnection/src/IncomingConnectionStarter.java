
import chain.incoming_connection.init.InitChain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class IncomingConnectionStarter {
    public static void main(String[] args) throws Exception {
        InetAddress hostAddress;
        int dataStreamPort, incomingConnectionPort, timeout;
        String moduleName;

        try {
            hostAddress = InetAddress.getByName(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("missing host address");
            e.printStackTrace();
            return;
        }
        try {
            dataStreamPort = Integer.parseInt(args[1]);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Missing data stream port");
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            System.err.println("given data for data stream module port is not an integer");
            e.printStackTrace();
            return;
        }
        try {
            incomingConnectionPort = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("given data for incoming connection module port is not an integer");
            e.printStackTrace();
            return;
        }catch (IndexOutOfBoundsException e){
            System.err.println("missing argument for incoming connection port");
            e.printStackTrace();
            return;
        }
        try {
            moduleName = args[3];
        } catch (IndexOutOfBoundsException e) {
            System.err.println("missing argument for name of this module");
            e.printStackTrace();
            return;
        }
        try {
            timeout = Integer.parseInt(args[4]);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("missing argument for timeout connection");
            e.printStackTrace();
            return;
        } catch (NumberFormatException e) {
            System.err.println("given data for timeout is not a number");
            e.printStackTrace();
            return;
        }

        new InitChain(hostAddress,
                dataStreamPort,
                incomingConnectionPort,
                moduleName,
                timeout)
                .resolve();
    }
}

class TestViettelInvoiceSend {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job", "SendInvoice");
        jsonObject.addProperty("clientId", "3C:7C:3F:2A:07:61");
        jsonObject.addProperty("username", "0101814245");
        jsonObject.addProperty("password", "123456aA@");
        jsonObject.addProperty("file", Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("/Users/thanhdo/Downloads/try.xlsx"))));

        bw.write(jsonObject.toString());
        bw.newLine();
        bw.flush();

        Gson gson = new Gson();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            System.out.println(line);
        }
    }
}

class TestViettelInvoiceGet {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job", "GetInvoice");
        jsonObject.addProperty("clientId", "f0:18:98:7b:72:f2");
        jsonObject.addProperty("username", "0101183303-007");
        jsonObject.addProperty("password", "123456aA@");
        jsonObject.addProperty("templateCode", "02GTTT0/001");
        jsonObject.addProperty("invoiceSeries", "AA/20E");
        jsonObject.addProperty("start", 11039);
        jsonObject.addProperty("end", 11039);

        bw.write(jsonObject.toString());
        bw.newLine();
        bw.flush();

        Gson gson = new Gson();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            System.out.println(line);
            JsonObject inputObject = gson.fromJson(line, JsonObject.class);
            File file = new File("/Users/thanhdo/Documents/" + inputObject.get("name").getAsString() + ".pdf");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(Base64.getDecoder().decode(inputObject.get("file").getAsString().getBytes(StandardCharsets.UTF_8)));
            fos.close();
        }
    }
}

class TestAuthentication {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String send = "{job: \"GetInvoice\", clientId:\"FC:34:97:C2:5A:2A\", start:\"abcdf\"}";
        bw.write(send);
        bw.newLine();
        bw.flush();

        System.out.println(br.readLine());
    }
}