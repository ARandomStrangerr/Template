package chain.viettel_invoice_send;

import chain.Link;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RejectChainCreateResponse extends Link<RejectChain> {
    public RejectChainCreateResponse(RejectChain chain) {
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
        if (chain.getProcessObject().has("return")) {
            JsonObject bodyObject = chain.getProcessObject().get("body").getAsJsonObject();
            JsonArray returnArray = bodyObject.get("return").getAsJsonArray();
            String errorMessage = bodyObject.get("response").getAsString() + "\n" +
                    "Tổng số hóa đơn thành công gửi đi " + returnArray.size() + " hóa đơn";
            if (returnArray.size() == 1) {
                errorMessage += "Số hóa đơn: " + returnArray.get(0).getAsJsonObject()
                        .get("result").getAsJsonObject()
                        .get("invoiceNo").getAsString();
            } else if (returnArray.size() > 1) {
                errorMessage += "\nSố hóa đơn bắt đầu: " + returnArray.get(0).getAsJsonObject()
                        .get("result").getAsJsonObject()
                        .get("invoiceNo").getAsString() +
                        "\nSố hóa đơn kết thúc: " + returnArray.get(0).getAsJsonObject()
                        .get("result").getAsJsonObject()
                        .get("invoiceNo").getAsString();
            }
            bodyObject.addProperty("response", errorMessage);
        }
        return true;
    }
}
