import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ViettelInvoiceDelete {
    public static void main(String[] args) throws Exception {
        String username = "0101183303-007",
                password = "123456aA@",
                verification = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        for (int i = 4124; i >= 2119; i--) {
            String invoiceNumber = String.format("AA/20E%07d", i);
            String sendData = "supplierTaxCode=0101183303-007&" +
                    "templateCode=02GTTT0%2F001&" +
                    "invoiceNo=" + URLEncoder.encode(invoiceNumber, StandardCharsets.UTF_8) + "&"+
                    "strIssueDate=20211110070000&" +
                    "additionalReferenceDesc=Văn bản thỏa thuận&" +
                    "additionalReferenceDate=20211120070000";
            HttpURLConnection con = (HttpURLConnection) new URL("https://api-sinvoice.viettel.vn:443/InvoiceAPI/InvoiceWS/cancelTransactionInvoice").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", verification);
            con.setRequestProperty("charset", "utf-8");
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.write(sendData.getBytes(StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println(reader.readLine());
            reader.close();
            dos.close();
            con.disconnect();
        }
    }
}
