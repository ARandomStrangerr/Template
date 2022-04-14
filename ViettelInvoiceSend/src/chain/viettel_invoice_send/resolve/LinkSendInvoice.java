package chain.viettel_invoice_send.resolve;

import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LinkSendInvoice extends Link<ResolveChain> {
    public LinkSendInvoice(ResolveChain chain) {
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
        // make authentication
        String username = chain.getProcessObject().get("body").getAsJsonObject()
                .get("username").getAsString(),
                password = chain.getProcessObject().get("body").getAsJsonObject()
                        .get("password").getAsString(),
                verification = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        // loop to send each request
        Gson gson = new Gson();
        int iterationNumber = 0;
        JsonArray returnInfo = new JsonArray();
        for (JsonElement element : chain.getProcessObject().get("body").getAsJsonObject().get("send").getAsJsonArray()) {
            // create the connection to the server
            HttpURLConnection con;
            try {
                con = (HttpURLConnection) new URL("https://api-sinvoice.viettel.vn:443/InvoiceAPI/InvoiceWS/createInvoice/" + username).openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", verification);
            } catch (IOException e) {
                System.err.println("Cannot established connection to the dedicated address");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số " + iterationNumber);
                e.printStackTrace();
                return false;
            }
            // create output stream
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            } catch (IOException e) {
                System.err.println("Cannot establish output stream with the address");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số " + iterationNumber);
                e.printStackTrace();
                return false;
            }
            // write to output stream
            try {
                writer.write(element.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.println("Cannot write output to stream");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số " + iterationNumber);
                e.printStackTrace();
                return false;
            }
            // create input stream
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                System.err.println("Cannot establish input stream with the address");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số " + iterationNumber);
                e.printStackTrace();
                return false;
            }
            // read input stream
            String returnData;
            try {
                returnData = reader.readLine();
            } catch (IOException e) {
                System.err.println("Cannot read from input stream");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", "Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số " + iterationNumber);
                e.printStackTrace();
                return false;
            }
            // convert the input into json object
            JsonObject returnObject = gson.fromJson(returnData, JsonObject.class);
            // analyze the received message to see if the other end unhappy with anything
            if (!returnObject.get("description").isJsonNull()) {
                System.err.println("The send data is invalid");
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", returnObject.get("description").getAsString() + " tại hóa đơn ở dòng số " + iterationNumber);
                return false;
            }
            // add the return info
            returnInfo.add(returnObject);
        }
        chain.getProcessObject().get("body").getAsJsonObject()
                .add("return", returnInfo);
        return true;
    }
}
