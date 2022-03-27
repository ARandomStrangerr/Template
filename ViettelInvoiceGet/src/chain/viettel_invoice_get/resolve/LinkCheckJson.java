package chain.viettel_invoice_get.resolve;

import chain.Link;
import com.google.gson.JsonObject;

class LinkCheckJson extends Link<ResolveChain> {
    LinkCheckJson(ResolveChain chain) {
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
        // check if the body of the Json object exists
        if (!chain.getProcessObject().has("body")) {
            chain.getProcessObject().addProperty("error", "Không tìm thấy body của package");
            System.err.println("body of the package is not found");
            return false;
        }
        // get the body of the Json object
        JsonObject body;
        try {
            body = chain.getProcessObject().get("body").getAsJsonObject();
        } catch (Exception e) {
            System.err.println("Cannot convert body to Json Object");
            e.printStackTrace();
            chain.getProcessObject().addProperty("error", "body của package không thể được convert thành Json Object");
            return false;
        }
        // check the username is contained on the package
        if (!body.has("username")) {
            chain.getProcessObject().addProperty("error", "Không tìm thấy tên người dùng để đăng nhập vào Viettel");
            System.err.println("username is not found");
            return false;
        }
        // check the password is contained on the package
        if (!body.has("password")) {
            chain.getProcessObject().addProperty("error", "Không tìm thấy mật khẩu để đăng nhập vào Viettel");
            System.err.println("password is not found");
            return false;
        }
        // check the start invoice number
        if (!body.has("start")) {
            chain.getProcessObject().addProperty("error", "Không tìm thấy số bắt đầu của hoá đơn tải về");
            System.err.println("start invoice number is not found");
            return false;
        }
        try {
            body.get("start").getAsInt();
        } catch (Exception e) {
            System.err.println("start number is not number format");
            e.printStackTrace();
            return false;
        }
        // check the end invoice number
        if (!body.has("end")) {
            chain.getProcessObject().addProperty("error", "Không tìm thấy số kết thúc của hoá đơn tải về");
            System.err.println("end invoice number is not found");
            return false;
        }
        try {
            body.get("end").getAsInt();
        } catch (Exception e) {
            System.err.println("end number is not number format");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
