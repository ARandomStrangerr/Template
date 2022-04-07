package chain.viettel_invoice_send.resolve;

import chain.Link;
import chain.viettel_invoice_send.ProcessChain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LinkMaterializeExcelFile extends Link<ProcessChain> {
    public LinkMaterializeExcelFile(ProcessChain chain) {
        super(chain);
    }

    /**
     * convert the Excel file send to the server into file to actually read it
     *
     * @return <code>true</code> when successfully run this block code
     * <code>false</code> when unsuccessfully run this block code
     */
    @Override
    protected boolean resolve() {
        // get the encoded file within the body then decode it
        byte[] encodedByteArray, decodedByteArray;
        try {
            encodedByteArray = chain.getProcessObject().get("body").getAsJsonObject()
                    .get("file").getAsString()
                    .getBytes(StandardCharsets.UTF_8);
        } catch (NullPointerException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response","Thông tin gởi đi thiếu tệp tin excel đính kèm");
            System.err.println("Missing the file object within the body request");
            e.printStackTrace();
            return false;
        }
        decodedByteArray = Base64.getDecoder().decode(encodedByteArray);
        // create file to hold the data
        File excelFile = new File(Thread.currentThread().getName());
        // write the decodedByteArray into the file
        try {
            FileOutputStream fos = new FileOutputStream(excelFile);
            fos.write(decodedByteArray);
            fos.close();
        } catch (IOException e) {
            chain.getProcessObject().get("body").getAsJsonObject()
                    .addProperty("response", "Không thể ghi tệp tin vui lòng liên hệ công ty phần mềm");
            System.out.println("Cannot write the file down");
            e.printStackTrace();
            return false;
        }
        // remove after done using
        this.chain.getProcessObject().get("body").getAsJsonObject()
                .remove("file");

        this.chain.setExcelFile(excelFile);
        return true;
    }
}
