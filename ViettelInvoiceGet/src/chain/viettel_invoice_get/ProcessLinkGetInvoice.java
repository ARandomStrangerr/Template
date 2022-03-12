package chain.viettel_invoice_get;

import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import connection_and_storage.connection.socket.PlainSocket;
import memorable.ViettelInvoiceGetMemorable;

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
        // create
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
        int start, end;
        try {
            start = chain.getProcessObject().get("body").getAsJsonObject()
                    .get("start").getAsInt();
        } catch (NullPointerException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Thiếu số bắt đầu");
            System.err.println("start number is null");
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Dữ liệu trường số bắt đầu không phải là số nguyên");
            System.err.println("start number is incorrect format");
            return false;
        }
        try {
            end = chain.getProcessObject().get("body").getAsJsonObject()
                    .get("end").getAsInt();
        } catch (NullPointerException e) {
            end = start;
        } catch (NumberFormatException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Dữ liệu trường số kết thúc đầu không phải là số nguyên");
            System.err.println("end number is incorrect format");
            return false;
        }
        JsonObject sendObject = new JsonObject();
        sendObject.addProperty("supplierTaxCode", username);
        sendObject.addProperty("fileType", "PDF");
        sendObject.addProperty("templateCode", chain.getProcessObject().get("body").getAsJsonObject().get("templateCode").getAsString());

        Gson gson = new Gson();
        PlainSocket outgoingConnection = ViettelInvoiceGetMemorable.getOutgoingConnection();
        while (start <= end) {
            sendObject.addProperty("invoiceNo", String.format("%s%07d", chain.getProcessObject().get("body").getAsJsonObject().get("invoiceSeries").getAsString(), start));
            // open connection
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) new URL("https://api-sinvoice.viettel.vn:443/InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile/").openConnection();
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
            // open writer
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            } catch (IOException e) {
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không kết nối được với máy chủ Viettel");
                System.err.println("Cannot open writer to viettel server");
                e.printStackTrace();
                return false;
            }
            // write to stream
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
            // open reader
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
            // read from reader and convert into json object
            JsonObject jsonInput;
            try {
                jsonInput = gson.fromJson(br.readLine(), JsonObject.class);
            } catch (IOException e) {
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không kết nối được với máy chủ Viettel");
                System.err.println("Cannot read from viettel server ");
                e.printStackTrace();
                return false;
            }
            // close reader , writer and https request
            try {
                bw.close();
                br.close();
            } catch (IOException e) {
                System.err.println("Cannot close connection");
            }
            con.disconnect();
            // write the received data into socket
            JsonObject jsonOutput = new JsonObject();
            jsonOutput.add("header", chain.getProcessObject().get("header"));
            JsonObject bodyOutput = new JsonObject();
            bodyOutput.add("name", jsonInput.get("fileName"));
            bodyOutput.add("file", jsonInput.get("fileToBytes"));
            jsonOutput.add("body", bodyOutput);

            try {
                outgoingConnection.write(jsonOutput.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            start++;
        }
        return true;
    }
}
