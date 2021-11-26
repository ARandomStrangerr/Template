package chain.viettel_invoice_delete;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class ProcessLinkSendRequest extends Link<ProcessChain> {
    public ProcessLinkSendRequest(ProcessChain chain) {
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
        String username = chain.getProcessObject().get("body").getAsJsonObject()
                .get("username").getAsString(),
                password = chain.getProcessObject().get("body").getAsJsonObject()
                        .get("password").getAsString(),
                verification = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        JsonArray deleteInvoiceArray = chain.getProcessObject().get("body").getAsJsonObject()
                .get("send").getAsJsonArray();
        int iteration = 1;
        for (JsonElement jsonElement : deleteInvoiceArray) {
            JsonObject deleteRequest = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> obj : deleteRequest.entrySet()){

            }

//            HttpURLConnection con = (HttpURLConnection) new URL("https://api-sinvoice.viettel.vn:443/InvoiceAPI/InvoiceWS/cancelTransactionInvoice").openConnection();
//            con.setDoOutput(true);
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            con.setRequestProperty("Authorization", verification);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
//            writer.write();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            iteration++;
        }

        return true;
    }
}
