package chain.viettel_invoice_send;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ProcessLinkCreateResponse extends Link<ProcessChain> {
    public ProcessLinkCreateResponse(ProcessChain chain) {
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
        JsonObject bodyObject = chain.getProcessObject().get("body").getAsJsonObject();
        JsonArray returnArray = bodyObject.get("return").getAsJsonArray();
        String message = "Thành công gởi đi " + returnArray.size() + " hóa đơn\n" +
                "Số hóa đơn bắt đầu: " + returnArray.get(0).getAsJsonObject()
                .get("result").getAsJsonObject()
                .get("invoiceNo").getAsString()
                + "\n" +
                "Số hóa đơn kết thúc: " + returnArray.get(returnArray.size() - 1).getAsJsonObject()
                .get("result").getAsJsonObject()
                .get("invoiceNo").getAsString();
        bodyObject.addProperty("response", message);
        return true;
    }
}
