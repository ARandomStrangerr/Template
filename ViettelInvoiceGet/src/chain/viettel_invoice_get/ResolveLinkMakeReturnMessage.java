package chain.viettel_invoice_get;

import chain.Link;
import com.google.gson.JsonObject;

public class ResolveLinkMakeReturnMessage extends Link<ResolveChain> {
    public ResolveLinkMakeReturnMessage(ResolveChain chain) {
        super(chain);
    }

    @Override
    protected boolean resolve() {
        chain.getProcessObject()
                .get("header")
                .getAsJsonObject()
                .addProperty("status", false);
        int start, end;
        start = chain.getProcessObject()
                .get("body")
                .getAsJsonObject()
                .get("start")
                .getAsInt();
        end = chain.getProcessObject()
                .get("body")
                .getAsJsonObject()
                .get("end")
                .getAsInt();
        JsonObject bodyObj = new JsonObject();
        bodyObj.addProperty("response", String.format("Thành công lấy về từ số %d đến số %d", start, end));
        chain.getProcessObject().add("body", bodyObj);
        return true;
    }
}
