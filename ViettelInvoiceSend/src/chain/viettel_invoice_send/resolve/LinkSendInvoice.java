package chain.viettel_invoice_send.resolve;

import chain.Link;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import memorable.ViettelInvoiceSend;

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
                .remove("username").getAsString(),
                password = chain.getProcessObject().get("body").getAsJsonObject()
                        .remove("password").getAsString(),
                verification = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        // loop to send each request
        Gson gson = new Gson();
        int iterationNumber = 0;
        JsonArray returnInfo = new JsonArray();
        for (JsonElement element : chain.getProcessObject().get("body").getAsJsonObject().get("send").getAsJsonArray()) {
            iterationNumber++;
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
                System.err.printf("Likely provided username / password is incorrect for %s at %d\n", username, iterationNumber);
                chain.getProcessObject()
                        .addProperty("error", String.format("Đã tạo %d hoá đơn. Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số %d", iterationNumber - 1, iterationNumber));
                e.printStackTrace();
                return false;
            }
            // create output stream
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            } catch (IOException e) {
                System.err.printf("Cannot establish output stream with the address for %s at %d\n", username, iterationNumber);
                chain.getProcessObject()
                        .addProperty("error", String.format("Đã tạo %d hoá đơn. Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số %d", iterationNumber - 1, iterationNumber));
                e.printStackTrace();
                return false;
            }
            // write to output stream
            try {
                writer.write(element.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.err.printf("Cannot write output to stream for %s at %d\n", username, iterationNumber);
                chain.getProcessObject()
                        .addProperty("error", String.format("Đã tạo %d hoá đơn. Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số %d", iterationNumber - 1, iterationNumber));
                e.printStackTrace();
                return false;
            }
            // create input stream
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                System.err.printf("Cannot establish input stream for %s at %d\n", username, iterationNumber);
                chain.getProcessObject()
                        .addProperty("error", String.format("Đã tạo %d hoá đơn. Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số %d", iterationNumber - 1, iterationNumber));
                e.printStackTrace();
                return false;
            }
            // read input stream
            String returnData;
            try {
                returnData = reader.readLine();
            } catch (IOException e) {
                System.err.printf("Cannot read from input stream for %s at %d\n", username, iterationNumber);
                chain.getProcessObject().get("body").getAsJsonObject()
                        .addProperty("response", String.format("Đã tạo %d hoá đơn. Không thể kết nối đến máy chủ Viettel tại hóa đơn ở dòng số %d", iterationNumber - 1, iterationNumber));
                e.printStackTrace();
                return false;
            }
            // convert the input into json object
            JsonObject returnObject = gson.fromJson(returnData, JsonObject.class);
            // analyze the received message to see if the other end unhappy with anything
            if (!returnObject.get("description").isJsonNull()) { // if there is an error, immediately stop the iteration then send the error back
                System.err.printf("The send data in line %d is invalid for %s with error message: %s\n", iterationNumber, username, returnObject.get("description").getAsString());
                chain.getProcessObject().remove("body");
                chain.getProcessObject().addProperty("error", String.format("Đã tạo %d hoá đơn. Lỗi tại dòng số %d trong tệp tin excel với tin nhắn: %s", iterationNumber - 1, iterationNumber, returnObject.get("description").getAsString()));
                return false;
            } else { // if there is no error, create a update message
                System.out.printf("Successfully create an invoice for %s with the following property %s\n", username, returnData);
                JsonObject updateObject = new JsonObject(),
                        updateHeader = new JsonObject(),
                        updateBody = new JsonObject();
                JsonArray toArray = new JsonArray();
                updateObject.add("header", updateHeader);
                updateObject.add("body", updateBody);
                updateHeader.addProperty("from", ViettelInvoiceSend.getInstance().getName());
                updateHeader.add("instance", chain.getProcessObject().get("header").getAsJsonObject().get("instance"));
                toArray.add("IncomingConnection");
                updateHeader.add("to", toArray);
                updateHeader.add("clientId", chain.getProcessObject().get("header").getAsJsonObject().get("clientId"));
                updateHeader.addProperty("status", true);
                updateHeader.addProperty("decrease", false);
                updateHeader.addProperty("terminate", false);
                updateBody.addProperty("update", String.format("Thành công tạo hoá đơn với số %s tại dòng số %d trong tệp tin excel", returnObject.get("result").getAsJsonObject().get("invoiceNo").getAsString(), iterationNumber));
                try {
                    ViettelInvoiceSend.getInstance().getSocket().write(updateObject.toString());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        chain.getProcessObject().get("body").getAsJsonObject().addProperty("response", String.format("Thành công tạo %d hoá đơn", iterationNumber));
        return true;
    }
}
