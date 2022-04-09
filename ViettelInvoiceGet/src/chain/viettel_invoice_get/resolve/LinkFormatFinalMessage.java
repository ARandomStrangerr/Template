package chain.viettel_invoice_get.resolve;

import chain.Link;
import com.google.gson.JsonObject;

public class LinkFormatFinalMessage extends Link<ResolveChain> {
    public LinkFormatFinalMessage(ResolveChain chain) {
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
        int start, end;
        start = chain.getProcessObject().get("body").getAsJsonObject().get("start").getAsInt();
        end = chain.getProcessObject().get("body").getAsJsonObject().get("end").getAsInt();
        JsonObject newBody = new JsonObject();
        newBody.addProperty("response", String.format("Thành công lấy về hoá đơn từ số %d đến số %d", start, end));
        chain.getProcessObject().add("body", newBody);
        return true;
    }
}
