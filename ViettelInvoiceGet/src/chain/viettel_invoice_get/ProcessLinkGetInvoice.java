package chain.viettel_invoice_get;

import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ProcessLinkGetInvoice extends Link<ProcessChain> {
    public ProcessLinkGetInvoice(ProcessChain chain) {
        super(chain);
    }

    /**
     * resolve the request within this chain
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        String username, password;
        try {
            username = chain.getProcessObject().get("body").getAsJsonObject()
                    .get("username").getAsString();
        } catch (NullPointerException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Tên đăng nhập chưa được điền");
            System.err.println("username is null");
            e.printStackTrace();
            return false;
        }
        try {
            password = chain.getProcessObject().get("body").getAsJsonObject()
                    .get("password").getAsString();
        } catch (NullPointerException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Mật khẩu chưa được điền");
            System.err.println("password is null");
            e.printStackTrace();
            return false;
        }
        String verification = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        JsonObject sendObject = new JsonObject();
        sendObject.addProperty("supplierTaxCode", username);
        sendObject.addProperty("fileType", "PDF");
        sendObject.addProperty("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode").getAsString());
        sendObject.addProperty("invoiceNo", chain.getProcessObject().get("body").getAsJsonObject().get("invoiceNo").getAsString());
        Gson gson = new Gson();
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) new URL("").openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", verification);
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không kết nối được với máy chủ Viettel");
            System.err.println("Cannot open connection to the viettel server");
            e.printStackTrace();
            return false;
        }
        BufferedWriter bw;
        try {
            bw = bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không kết nối được với máy chủ Viettel");
            System.err.println("Cannot open writer to viettel server");
            e.printStackTrace();
            return false;
        }
        try {
            bw.write(sendObject.toString());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không kết nối được với máy chủ Viettel");
            System.err.println("Cannot write to viettel server");
            e.printStackTrace();
            return false;
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không kết nối được với máy chủ Viettel");
            System.err.println("Cannot open reader to viettel server");
            e.printStackTrace();
            return false;
        }
        try {
            br.readLine();
        } catch (IOException e) {

            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không kết nối được với máy chủ Viettel");
            System.err.println("Cannot read from viettel server ");
            e.printStackTrace();
            return false;
        }
        try {
            bw.close();
            br.close();
        } catch (IOException e) {
            System.err.println("Cannot close connection");
        }
        con.disconnect();

        return false;
    }
}
