package chain.viettel_invoice_get.resolve;

import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import memorable.ViettelInvoiceGet;
import socket.Socket;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class LinkGetInvoice extends Link<ResolveChain> {
    public LinkGetInvoice(ResolveChain chain) {
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
        JsonObject body = chain.getProcessObject().get("body").getAsJsonObject();
        int start = body.get("start").getAsInt(), // starting iteration
                end = body.get("end").getAsInt(); // ending iteration
        String address = "https://api-sinvoice.viettel.vn:443/InvoiceAPI/InvoiceUtilsWS/getInvoiceRepresentationFile/", // address to connect to
                username = body.get("username").getAsString(), // username to login into viettel server
                password = body.get("password").getAsString(), // password to login into viettel server
                verification = String.format("Basic %s", Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8))), // authentication code to login based on username and password
                invoiceSeries = body.get("invoiceSeries").getAsString(); // invoice series of the invoice
        JsonObject sendObject = new JsonObject(); // the object which is used to send data to viettel
        sendObject.addProperty("supplierTaxCode", username);
        sendObject.addProperty("fileType", "PDF");
        sendObject.addProperty("templateCode", body.get("templateCode").getAsString());
        Gson gson = new Gson(); // object use to send data
        Socket socket = ViettelInvoiceGet.getInstance().getSocket();
        for (int index = start; index <= end; index++) { // request for each inoice
            sendObject.addProperty("invoiceNo", String.format("%s%07d", invoiceSeries, index)); // set the invoice name to the paackage
            // step open connections to the address
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) new URL(address).openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", verification);
            } catch (IOException e) {
                chain.getProcessObject().addProperty("error", "Không kết nối được đến máy chủ Viettel");
                System.err.println("Not able to open connection to Viettel server");
                e.printStackTrace();
                return false;
            }
            // open writing channel
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
                writer.write(sendObject.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                chain.getProcessObject().addProperty("error", "Không thiết lập được giáo thức đến máy chủ Viettel");
                System.err.println("Not able to open output stream to Viettel server");
                e.printStackTrace();
                return false;
            }
            // open reading channel
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                chain.getProcessObject().addProperty("error", "Không thiếp lập được giao thức đến máy chủ Viettel");
                System.err.println("Not able to open input stream to Viettel server");
                e.printStackTrace();
                return false;
            }
            // read data then close connection
            String inputData;
            try {
                inputData = reader.readLine();
                writer.close();
                reader.close();
                con.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            // turn string object into json object
            JsonObject returnObject;
            try {
                returnObject = gson.fromJson(inputData, JsonObject.class);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            // send the data back immediately as an update after successfully receive it.
            JsonObject updateObject = new JsonObject(),
                    bodyUpdateObject = new JsonObject();
            bodyUpdateObject.add("name", returnObject.get("fileName"));
            bodyUpdateObject.add("file", returnObject.get("fileToByte"));
            updateObject.add("header", chain.getProcessObject().get("header"));
            updateObject.add("body", bodyUpdateObject);
            try {
                socket.write(updateObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}